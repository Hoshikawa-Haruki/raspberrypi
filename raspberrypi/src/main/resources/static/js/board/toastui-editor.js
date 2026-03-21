/* global toastui */

document.addEventListener("DOMContentLoaded", () => {

    // 페이지 로드 시 idemKey 1회 생성
    const idemKey = crypto.randomUUID();
    document.getElementById("idempotencyKey").value = idemKey;

    const configEl = document.getElementById("editor-config");
    const uploadUrl = configEl.dataset.uploadUrl;

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

    async function uploadImage(file, isPaste = false) {
        const MAX_SIZE = 10 * 1024 * 1024;

        if (file.size > MAX_SIZE) {
            alert("이미지 크기는 10MB 이하만 업로드 가능합니다.");
            return;
        }

        const ext = file.type.split('/')[1] || 'png';

        let filename;

        if (isPaste) {
            // ✅ paste일 때만 강제 파일명
            filename = 'paste-' + Date.now() + '.' + ext;
        } else {
            // ✅ 일반 업로드는 원래 이름 유지
            filename = file.name;
        }

        const formData = new FormData();
        formData.append("image", file, filename);

        const res = await fetch(uploadUrl, {
            method: "POST",
            body: formData
        });

        const data = await res.json();

        if (data.success) {
            editor.exec('addImage', {
                imageUrl: data.url,
                altText: filename
            });
        } else {
            alert("이미지 업로드 실패");
        }
    }

    /* 커스텀 이미지 업로드 처리 함수 */
    function openImageDialog() {
        const MAX_SIZE = 10 * 1024 * 1024;

        const fileInput = document.createElement('input');
        fileInput.type = 'file';
        fileInput.accept = 'image/*'; // 이미지 타입만 허용
        fileInput.multiple = true;

        fileInput.onchange = async (e) => {
            for (const file of e.target.files) {

                await uploadImage(file, false);
            }
        };

        fileInput.click();
    }

    /* 제출 시 HTML 저장 */
    const form = document.getElementById("postForm");
    if (form) {
        form.addEventListener("submit", () => {
            document.getElementById("content").value = window.editor.getHTML();
        });
    }

});
