<%-- 
    Document   : update.jsp
    Created on : 2025. 10. 23., 오후 6:43:23
    Author     : Haruki
--%>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
                    <p><strong>기존 첨부파일</strong></p>
                    <c:forEach var="file" items="${post.attachments}">
                        <c:set var="physicalName" value="${file.uuid}.${file.ext}"/>

                        <label>
                            <input type="checkbox" name="deleteFileIds" value="${file.id}">
                            (삭제)
                        </label>

                        <c:choose>
                            <c:when test="${file.ext == 'png' or file.ext == 'jpg' or file.ext == 'jpeg' or file.ext == 'gif'}">
                                <img src="${pageContext.request.contextPath}/upload/${physicalName}"
                                     alt="${file.uuid}"
                                     style="max-width:200px; display:block; margin:3px 0;">
                            </c:when>
                            <c:otherwise>
                                ${file.originalName}
                            </c:otherwise>
                        </c:choose>
                        <br/>
                    </c:forEach>
                    <hr/>
                </c:if>

                <!-- 새 첨부 -->
                <input type="file" name="newFiles" multiple><br/><br/>
                <input type="hidden" name="_csrf" value="${_csrf.token}">

                <button type="submit">수정</button>
                <button type="button"
                        onclick="location.href = '${pageContext.request.contextPath}/board/list'">취소</button>
            </form>
        </div>

        <!-- Toast UI Editor -->
        <script>
            const originalContent = document.getElementById("originContent").value;

            const editor = new toastui.Editor({
                el: document.querySelector('#editor'),
                height: '600px',
                initialEditType: 'wysiwyg',
                previewStyle: 'vertical',
                initialValue: originalContent, // 기존 글 넣기
                hooks: {// 이미지 업로드 후 URL 삽입을 처리하는 훅
                    addImageBlobHook: async (blob, callback) => {

                        const formData = new FormData();
                        formData.append("image", blob);

                        const res = await fetch("${pageContext.request.contextPath}/upload/image", {
                            method: "POST",
                            body: formData
                        });

                        const data = await res.json();

                        if (data.success) {
                            callback(data.url, "image");
                        } else {
                            alert("이미지 업로드 실패");
                        }
                    }
                }
            });
            // 제출 시 HTML을 textarea에 넣기
            document.getElementById("postForm").addEventListener("submit", function () {
                document.getElementById("content").value = editor.getHTML();
            });
        </script>
    </body>
</html>
