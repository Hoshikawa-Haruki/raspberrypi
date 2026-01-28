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
                       placeholder="제목을 입력해 주세요." required>

                <!-- 에디터 -->
                <div id="editor"></div>
                <!-- 서버로 전송될 HTML -->
                <textarea id="content" name="content" style="display:none;"></textarea>
                <!-- 첨부파일 --> 
                <input type="file" name="files" multiple>
                <!-- csrf 토큰 전송 -->
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                <div class="form-actions">
                    <button type="submit" class="btn-submit">등록</button>
                    <button type="button" class="btn-cancel"
                            onclick="location.href = '${pageContext.request.contextPath}/board/list'">
                        취소
                    </button>
                </div>

                <!-- idem 키 전송 -->
                <input type="hidden" name="idempotencyKey" id="idempotencyKey">
            </form>
        </div>

        <!-- JS로 경로 전달 -->
        <div id="editor-config"
             data-upload-url="${pageContext.request.contextPath}/upload/temp">
        </div>

        <!-- write 전용 JS -->
        <script src="${pageContext.request.contextPath}/js/board/toastui-editor.js"></script>
    </body>
</html>