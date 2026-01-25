<%-- 
    Document   : update_toastui_ver2
    Created on : 2025. 11. 24., 오전 1:23:37
    Author     : Haruki
--%>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <title>짭케 마이너 갤러리</title>
        <jsp:include page="/WEB-INF/views/board/head.jsp" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/write.css">
        <link rel="stylesheet" href="https://uicdn.toast.com/editor/latest/toastui-editor.min.css">
        <script src="https://uicdn.toast.com/editor/latest/toastui-editor-all.min.js"></script>
    </head>
    <body>
        <jsp:include page="/WEB-INF/views/board/top_common_menu.jsp" />
        <div class="container">
            <h2>✏️ 위지윅 수정</h2>

            <form id="postForm" method="post" enctype="multipart/form-data"
                  action="${pageContext.request.contextPath}/board/update/${post.id}">

                <input type="text" name="title" maxlength="40" value="${post.title}"
                       placeholder="제목을 입력해 주세요." required><br/><br/>

                <!-- 에디터 -->
                <div id="editor"></div><br/>
                <!-- 서버에서 가져온 본문 HTML -->
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
                <input type="file" name="newFiles" multiple><br/><br/>
                <input type="hidden" name="_csrf" value="${_csrf.token}">

                <button type="submit">수정</button>
                <button type="button"
                        onclick="location.href = '${pageContext.request.contextPath}/board/list'">취소</button>
            </form>
        </div>

        <!-- 기존 글 HTML 전달 -->
        <textarea id="originContent" style="display:none;">
            ${post.content}
        </textarea>

        <!-- JS로 경로 전달 -->
        <div id="editor-config"
             data-upload-url="${pageContext.request.contextPath}/upload/temp">
        </div>

        <!-- update 전용 JS -->
        <script src="${pageContext.request.contextPath}/js/board/toastui-editor.js"></script>

    </body>
</html>

