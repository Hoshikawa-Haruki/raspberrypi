/* global toastui */

document.addEventListener("DOMContentLoaded", () => {

    // 페이지 로드 시 idemKey 1회 생성
    const idemKey = crypto.randomUUID();
    document.getElementById("idempotencyKey").value = idemKey;

    const configEl = document.getElementById("editor-config");
    const uploadUrl = configEl.dataset.uploadUrl;

    // 제한값을 서버(application.properties)에서 data attribute로 전달받음
    // 하드코딩 금지 - 수치 변경 시 application.properties만 수정하면 됨
    const MAX_SIZE             = parseInt(configEl.dataset.maxSingleImageSize);      // 이미지 1장 최대 크기
    const MAX_TOTAL_SIZE       = parseInt(configEl.dataset.maxInlineSize);           // 인라인 이미지 총 용량 제한
    const MAX_ATTACH_SIZE      = parseInt(configEl.dataset.maxAttachmentSize);       // 첨부파일 총 용량 제한
    const EXISTING_INLINE_SIZE = parseInt(configEl.dataset.existingInlineSize) || 0; // 수정 시 기존 인라인 이미지 총 용량 (신규 작성 시 0)

    /**
     * 현재 에디터에 존재하는 인라인 이미지를 uuid → fileSize(bytes) 형태로 관리하는 Map
     * - 이미지 업로드 성공 시 추가
     * - 에디터 change 이벤트 발생 시 본문에 없는 uuid는 자동 제거
     * → 이미지를 삭제하면 해당 용량이 차감되어 총합이 정확하게 유지됨
     */
    const uploadedImages = new Map(); // uuid → fileSize (bytes)

    // update 페이지면 originContent(본문 내용) 사용
    const originEl = document.getElementById("originContent");
    const initialValue = originEl ? originEl.value : "";

    /* ===============================
     * Toast UI Editor 초기화
     * =============================== */
    window.editor = new toastui.Editor({
        el: document.querySelector('#editor'), // jsp div와 바인딩
        height: '800px',
        initialEditType: 'wysiwyg',
        previewStyle: 'vertical',
        language: 'ko-KR',
        initialValue, // write면 "", update면 기존 글

        /* 기본 이미지 버튼 제거 */
        toolbarItems: [
            ['heading', 'bold', 'italic', 'strike'],
            ['hr', 'quote'],
            ['ul', 'ol', 'task'],
            ['table', 'link'],
            [{
                    name: 'customImage',
                    tooltip: '이미지 업로드',
                    el: (() => {
                        const btn = document.createElement('button');
                        btn.type = 'button';
                        btn.className = 'toastui-editor-toolbar-icons image';
                        btn.style.margin = '0 6px';
                        btn.addEventListener('click', openImageDialog);
                        return btn;
                    })()
                }],
            ['code', 'codeblock']
        ]
    });

    /**
     * 에디터 본문 HTML에서 임시 업로드 이미지의 uuid 목록을 추출하는 함수
     * 서버의 InlineImageService.extractImageUuids()와 동일한 패턴 사용
     * @returns {Set<string>} 현재 본문에 존재하는 uuid Set
     */
    function extractUuidsFromContent() {
        const html = window.editor.getHTML();
        const pattern = /\/upload_temp\/([a-zA-Z0-9\-]+)\.(png|jpg|jpeg|gif|webp)/gi;
        const uuids = new Set();
        let match;
        while ((match = pattern.exec(html)) !== null) {
            uuids.add(match[1]);
        }
        return uuids;
    }

    /**
     * 에디터 내용이 변경될 때마다 uploadedImages Map을 본문 기준으로 동기화하는 함수
     * 본문에 더 이상 존재하지 않는 uuid(삭제된 이미지)를 Map에서 제거하여
     * 총 용량 계산이 항상 현재 상태를 정확히 반영하도록 함
     */
    function syncUploadedImages() {
        const currentUuids = extractUuidsFromContent();
        for (const uuid of uploadedImages.keys()) {
            if (!currentUuids.has(uuid)) {
                uploadedImages.delete(uuid); // 본문에 없으면 Map에서 제거 (용량 차감)
            }
        }
    }

    /**
     * 현재 uploadedImages Map에 등록된 이미지들의 총 용량을 계산하는 함수
     * @returns {number} 총 용량 (bytes)
     */
    function calcTotalSize() {
        return EXISTING_INLINE_SIZE + [...uploadedImages.values()].reduce((sum, size) => sum + size, 0);
    }

    function updateUsageDisplay() {
        const el = document.getElementById('inline-image-usage');
        if (!el) return;
        const usedMB = (calcTotalSize() / (1024 * 1024)).toFixed(1);
        const limitMB = (MAX_TOTAL_SIZE / (1024 * 1024)).toFixed(0);
        el.textContent = `이미지 용량: ${usedMB}MB / ${limitMB}MB`;
    }

    // 에디터 내용 변경 시마다 Map 동기화 (이미지 삭제 감지)
    window.editor.on('change', () => {
        syncUploadedImages();
        updateUsageDisplay();
    });

    // 페이지 로드 시 초기 용량 표시 (수정 페이지에서 기존 이미지 용량 반영)
    updateUsageDisplay();

    const editorRoot = document.querySelector('#editor');

    // Toast UI 내부 handler보다 먼저 실행, base64 삽입 전에 가로챔
    // copy & paste
    editorRoot.addEventListener('paste', async (e) => {

        const clipboard = e.clipboardData;
        if (!clipboard)
            return;

        const items = clipboard.items || [];
        for (const item of items) {
            if (!item.type.startsWith('image/')) // 이미지만
                continue;
            // Toast UI 기본 paste 차단 (base64 차단)
            e.preventDefault();
            e.stopPropagation();
            e.stopImmediatePropagation();

            const blob = item.getAsFile();
            if (!blob)
                return;

            await uploadImage(blob, true);
            return;
        }
    }, true);

    // drag & drop
    editorRoot.addEventListener('drop', async (e) => {
        e.preventDefault();
        e.stopPropagation();

        const files = e.dataTransfer?.files;
        if (!files)
            return;

        for (const file of files) {
            if (!file.type.startsWith('image/'))
                continue;
            await uploadImage(file, false);
        }
    }, true);

    /**
     * 이미지를 서버 temp 폴더에 업로드하는 함수
     * 업로드 전 1장 크기 / 총합 용량을 검사하고,
     * 성공 시 uuid → fileSize를 Map에 등록하여 총합 추적
     * @param {File} file - 업로드할 이미지 파일
     * @param {boolean} isPaste - 붙여넣기 여부 (true면 파일명 강제 생성)
     */
    async function uploadImage(file, isPaste = false) {

        // 1장 크기 체크
        if (file.size > MAX_SIZE) {
            alert(`이미지 1장의 크기는 ${MAX_SIZE / (1024 * 1024)}MB를 초과할 수 없습니다.`);
            return;
        }

        // 총합 용량 체크 (현재 Map 기준 계산)
        if (calcTotalSize() + file.size > MAX_TOTAL_SIZE) {
            alert(`이미지 총 용량은 ${MAX_TOTAL_SIZE / (1024 * 1024)}MB를 초과할 수 없습니다.`);
            return;
        }

        const ext = file.type.split('/')[1] || 'png';
        const filename = isPaste ? 'paste-' + Date.now() + '.' + ext : file.name;

        const formData = new FormData();
        formData.append("image", file, filename);

        const res = await fetch(uploadUrl, {
            method: "POST",
            body: formData
        });

        const data = await res.json();

        if (data.success) {
            // URL에서 uuid 추출 후 Map에 등록 (/upload_temp/{uuid}.{ext} 형식)
            const uuid = data.url.split('/').pop().split('.')[0];
            uploadedImages.set(uuid, file.size);
            updateUsageDisplay();

            editor.exec('addImage', {
                imageUrl: data.url,
                altText: filename
            });
        } else {
            alert("이미지 업로드 실패");
        }
    }

    /**
     * 툴바 커스텀 이미지 버튼 클릭 시 파일 선택 창을 여는 함수
     * 선택한 파일들을 uploadImage()로 순차 업로드
     */
    function openImageDialog() {
        const fileInput = document.createElement('input');
        fileInput.type = 'file';
        fileInput.accept = 'image/*';
        fileInput.multiple = true;

        fileInput.onchange = async (e) => {
            for (const file of e.target.files) {
                await uploadImage(file, false);
            }
        };

        fileInput.click();
    }

    /**
     * 폼 제출 시 첨부파일 총 용량을 검사하는 이벤트 핸들러
     * MAX_ATTACH_SIZE 초과 시 제출을 차단하여 서버 에러 없이 사용자에게 안내
     * (서버의 MaxUploadSizeExceededException에 대한 클라이언트 1차 방어선)
     */
    const form = document.getElementById("postForm");
    if (form) {
        form.addEventListener("submit", (e) => {
            const totalFileSize = [...form.querySelectorAll('input[type="file"]')]
                .flatMap(input => [...input.files])
                .reduce((sum, file) => sum + file.size, 0);

            if (totalFileSize > MAX_ATTACH_SIZE) {
                e.preventDefault();
                alert(`첨부파일 총 용량은 ${MAX_ATTACH_SIZE / (1024 * 1024)}MB를 초과할 수 없습니다.`);
                return;
            }
            document.getElementById("content").value = window.editor.getHTML();
        });
    }

});
