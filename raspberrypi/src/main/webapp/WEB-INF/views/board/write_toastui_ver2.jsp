<%-- 
    Document   : write_toastui_new
    Created on : 2025. 11. 24., 오후 1:40:53
    Author     : Haruki

    기존 ToastUI + 다중 이미지 첨부 구현 write.jsp
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>짭케 마이너 갤러리</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
        <link rel="stylesheet" type="text/css"
              href="${pageContext.request.contextPath}/css/write.css">
        <link rel="stylesheet" href="https://uicdn.toast.com/editor/latest/toastui-editor.min.css">
        <script src="https://uicdn.toast.com/editor/latest/toastui-editor-all.min.js"></script>
        <script src="https://uicdn.toast.com/editor/latest/i18n/ko-kr.js"></script>
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

            /* 커스텀 이미지 업로드 처리 함수 */
            function openImageDialog() {
                const MAX_SIZE = 10 * 1024 * 1024; // 5MB

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

                        const data = await res.json();

                        if (data.success) {
                            editor.exec('addImage', {
                                imageUrl: data.url,
                                altText: file.name
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