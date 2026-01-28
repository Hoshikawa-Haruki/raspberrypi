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
    const editor = new toastui.Editor({
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

    // Ctrl+V 이미지 붙여넣기 처리
    const editorRoot = document.querySelector('#editor');

    // Toast UI 내부 handler보다 먼저 실행, base64 삽입 전에 가로챔
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

            const MAX_SIZE = 10 * 1024 * 1024;
            if (blob.size > MAX_SIZE) {
                alert("이미지 크기는 10MB 이하만 업로드 가능합니다.");
                return;
            }

            const ext = blob.type.split('/')[1]; // image/png → png 확장자 추출
            const fakeName = 'gov-' + Date.now() + '.' + ext; // 임시파일명 (기본은 image로 나옴)

            const formData = new FormData(); // 멀티파트 요청 컨테이너
            formData.append("image", blob, fakeName);
            // image : 업로드 요청의 request parameter명 (@image)

            const res = await fetch(uploadUrl, {// 서버 이미지 업로드
                method: "POST",
                body: formData
            });

            const data = await res.json();

            if (data.success) {
                editor.exec('addImage', {
                    imageUrl: data.url,
                    altText: 'pasted-image'
                });
            } else {
                alert("이미지 업로드 실패");
            }

            return;
        }
    }, true);

    /* 커스텀 이미지 업로드 처리 함수 */
    function openImageDialog() {
        const MAX_SIZE = 10 * 1024 * 1024;

        const fileInput = document.createElement('input');
        fileInput.type = 'file';
        fileInput.accept = 'image/*'; // 이미지 타입만 허용
        fileInput.multiple = true;

        fileInput.onchange = async (e) => {
            for (const file of e.target.files) {

                if (file.size > MAX_SIZE) {
                    alert("이미지 크기는 10MB 이하만 업로드 가능합니다.");
                    continue;
                }

                const formData = new FormData();
                formData.append("image", file);

                const res = await fetch(uploadUrl, {
                    method: "POST",
                    body: formData // multipart 요청
                });

                const data = await res.json();

                if (data.success) {
                    editor.exec('addImage', {
                        imageUrl: data.url, // 파일경로
                        altText: file.name // 파일명
                    });
                } else {
                    alert("이미지 업로드 실패");
                }
            }
        };

        fileInput.click();
    }

    /* 제출 시 HTML 저장 */
    const form = document.getElementById("postForm");
    if (form) {
        form.addEventListener("submit", () => {
            document.getElementById("content").value = editor.getHTML();
        });
    }

});
