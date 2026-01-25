<%-- 
    Document   : write_toastui_new
    Created on : 2026. 1. 26.
    Author     : Haruki

    js script 분리 전 LEGACY
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>짭케 마이너 갤러리</title>
        <jsp:include page="/WEB-INF/views/board/head.jsp" />
        <link rel="stylesheet" href="https://uicdn.toast.com/editor/latest/toastui-editor.min.css">
        <script src="https://uicdn.toast.com/editor/latest/toastui-editor-all.min.js"></script>
        <script src="https://uicdn.toast.com/editor/latest/i18n/ko-kr.js"></script>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/write.css">
    </head>

    <body>
        <jsp:include page="/WEB-INF/views/board/top_common_menu.jsp" />

        <div class="container">
            <h2>✏️ 위지윅 작성</h2>

            <form id="postForm" method="post" enctype="multipart/form-data"
                  action="${pageContext.request.contextPath}/board/save">

                <input type="text" name="title" maxlength="40" 
                       value="${post.title}"
                       placeholder="제목을 입력해 주세요." required><br/><br/>

                <!-- 에디터 -->
                <div id="editor"></div><br/>
                <!-- 서버로 전송될 HTML -->
                <textarea id="content" name="content" style="display:none;"></textarea>
                <!-- 첨부파일 --> 
                <input type="file" name="files" multiple><br/><br/>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button type="submit">등록</button>
                <button type="button" class="btn btn-cancel"
                        onclick="location.href = '${pageContext.request.contextPath}/board/list'">취소</button>

            </form>
        </div>

        <!-- Toast UI Editor -->
        <script>
            const editor = new toastui.Editor({
                el: document.querySelector('#editor'),
                height: '800px',
                initialEditType: 'wysiwyg',
                previewStyle: 'vertical',
                language: 'ko-KR',

                /* 기본 이미지 버튼 제거 */
                toolbarItems: [
                    ['heading', 'bold', 'italic', 'strike'],
                    ['hr', 'quote'],
                    ['ul', 'ol', 'task'],
                    ['table', 'link'],
                    // 툴바 커스텀 이미지 버튼
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
                    if (item.type.startsWith('image/')) { // 이미지만
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

                        const formData = new FormData(); // 멀티파트 요청 컨테이너
                        const ext = blob.type.split('/')[1];   // image/png → png 확장자 추출
                        const fakeName = 'gov-' + Date.now() + '.' + ext; // 임시파일명 (기본은 image로 나옴)
                        formData.append("image", blob, fakeName);
                        // image : 업로드 요청의 request parameter명 (@image)


                        const res = await fetch(// 서버 이미지 업로드
                                "${pageContext.request.contextPath}/upload/temp",
                                {method: "POST", body: formData}
                        );

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
                }
            }, true);

            /* 커스텀 이미지 업로드 처리 함수 */
            function openImageDialog() {
                const MAX_SIZE = 10 * 1024 * 1024; // 10MB

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

                        const res = await fetch("${pageContext.request.contextPath}/upload/temp", {
                            method: "POST",
                            body: formData // multipart 요청
                        });

                        // 서버 응답 받기
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
            document.getElementById("postForm").addEventListener("submit", function () {
                document.getElementById("content").value = editor.getHTML();
            });
        </script>
    </body>
</html>