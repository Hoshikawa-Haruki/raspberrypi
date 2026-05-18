<%-- 
    Document   : update_toastui_ver2
    Created on : 2025. 11. 24., 오전 1:23:37
    Author     : Haruki
--%>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <title>RetroDays</title>
        <jsp:include page="/WEB-INF/views/board/head.jsp" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/write.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
        <link rel="stylesheet" href="https://uicdn.toast.com/editor/latest/toastui-editor.min.css">
        <script src="https://uicdn.toast.com/editor/latest/toastui-editor-all.min.js"></script>
    </head>
    <body>
        <header>
            <jsp:include page="/WEB-INF/views/board/top_common_menu.jsp" />
        </header>
        <div class="container">
            <h2>✏️ 게시글 수정</h2>

            <form id="postForm" method="post" enctype="multipart/form-data"
                  action="${pageContext.request.contextPath}/board/update/${post.id}">

                <input type="text" name="title" class="title-input" maxlength="40" value="${post.title}"
                       placeholder="제목을 입력해 주세요." required>

                <!-- 에디터 -->
                <div id="editor"></div>
                <div id="inline-image-usage" style="font-size:0.85rem; color:#888; text-align:right; margin-top:4px;">
                    이미지 용량: 0MB / -MB
                </div>
                <!-- 기존 본문 HTML -->
                <textarea id="originContent" style="display:none;">${post.content}</textarea>
                <!-- 최종 전송용 -->
                <textarea id="content" name="content" style="display:none;"></textarea>

                <!-- 기존 첨부파일 목록 -->
                <c:if test="${not empty post.attachments}">

                    <!-- FILE 첨부파일 개수 계산 -->
                    <c:set var="fileCount" value="0" />
                    <c:forEach var="file" items="${post.attachments}">
                        <c:if test="${file.type.name() eq 'FILE'}">
                            <c:set var="fileCount" value="${fileCount + 1}" />
                        </c:if>
                    </c:forEach>

                    <!-- FILE 첨부파일이 있을 때 -->
                    <c:if test="${fileCount > 0}">
                        <p><strong>기존 첨부파일</strong></p>
                        <c:forEach var="file" items="${post.attachments}">
                            <c:if test="${file.type.name() eq 'FILE'}">
                                <label>
                                    <input type="checkbox" name="deleteFileIds" value="${file.id}"> (삭제)
                                    ${file.originalName}
                                </label>
                            </c:if>
                        </c:forEach>

                        <hr/>
                    </c:if>

                    <!-- FILE 첨부파일이 없을 때 -->
                    <c:if test="${fileCount == 0}">
                        <p style="color: #d32f2f;">첨부파일 없음</p>
                    </c:if>

                </c:if>

                <!-- 새 첨부 -->
                <input type="file" name="newFiles" multiple>
                <!-- csrf 토큰 전송 -->
                <input type="hidden" name="_csrf" value="${_csrf.token}">

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

        <%--
            JS로 서버 설정값 전달용 div (화면에 보이지 않음)
            - data-upload-url           : 인라인 이미지 임시 업로드 엔드포인트
            - data-max-inline-size      : 인라인 이미지 총 용량 제한 (바이트, application.properties: file.max-inline-image-size)
            - data-max-attachment-size  : 첨부파일 총 용량 제한 (바이트, application.properties: file.max-attachment-size)
            - data-existing-inline-size : 기존 인라인 이미지 총 용량 (바이트, 수정 시 초기값으로 사용)
            값은 GlobalUserModelAdvice → FileProperties → application.properties 순으로 주입됨
        --%>
        <div id="editor-config"
             data-upload-url="${pageContext.request.contextPath}/upload/temp"
             data-max-single-image-size="${maxSingleImageSize}"
             data-max-inline-size="${maxInlineImageSize}"
             data-max-attachment-size="${maxAttachmentSize}"
             data-existing-inline-size="${existingInlineImageSize}">
        </div>

        <!-- update 전용 JS -->
        <script src="${pageContext.request.contextPath}/js/board/toastui-editor.js"></script>

        <footer class="site-footer">
            <jsp:include page="/WEB-INF/views/board/footer.jsp"/>
        </footer>
    </body>
</html>

