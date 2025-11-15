<%-- 
    Document   : write
    Created on : 2025. 10. 13., 오후 1:40:53
    Author     : Haruki
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

    </head>
    <body>
        <div class="container">
            <h2>✏️ 위지윅 작성</h2>

            <form id="postForm" method="post" enctype="multipart/form-data"
                  action="${pageContext.request.contextPath}/board/save">

                <label>제목:</label>
                <input type="text" name="title" maxlength="40" required><br/><br/>

                <label>첨부파일:</label>
                <input type="file" name="files" multiple><br/><br/>

                <!-- 에디터가 들어갈 자리 -->
                <div id="editor"></div>
                <!-- 서버로 전송될 HTML -->
                <textarea id="content" name="content" style="display:none;"></textarea>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button type="submit">등록</button>
                <button type="button"
                        onclick="location.href = '${pageContext.request.contextPath}/board/list'">취소</button>

            </form>
        </div>
        <!-- Toast UI Editor 초기화 -->
        <script>
            const editor = new toastui.Editor({
                el: document.querySelector('#editor'),
                height: '600px',
                initialEditType: 'markdown',
                previewStyle: 'vertical'
            });

            // ✔ 제출 시 HTML을 textarea로 옮김
            document.getElementById("postForm").addEventListener("submit", function () {
                document.getElementById("content").value = editor.getHTML();
            });
        </script>
    </body>
</html>