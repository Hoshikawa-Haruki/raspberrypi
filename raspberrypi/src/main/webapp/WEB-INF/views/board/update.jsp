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
    </head>
    <body>
        <h2>✏️ 게시글 수정</h2>

        <form method="post" enctype="multipart/form-data"
              action="${pageContext.request.contextPath}/board/update/${post.id}">


            <!-- 작성자 (수정불가) -->
            <label>작성자:</label>
            <input type="text" value="${post.author}" readonly><br/><br/>

            <!-- 기존 저장된 비밀번호 (수정불가-전달X) -->
            <!-- TODO : 수정 화면 진입 시 비밀번호 검증 로직 추가 필요-->
            <label>비밀번호:</label>
            <input type="password" value="${post.password}" readonly><br/><br/>


            <label>제목:</label>
            <input type="text" name="title" maxlength="40"
                   value="${post.title}" required><br/><br/>

            <label>내용:</label><br/>
            <textarea name="content" rows="10" cols="60" required>${post.content}</textarea><br/><br/>

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
                            <a href="${pageContext.request.contextPath}/download/${file.uuid}">
                                ${file.originalName}
                            </a>
                        </c:otherwise>
                    </c:choose>
                    <br/>
                </c:forEach>
                <hr/>
            </c:if>

            <!-- 새 첨부 -->
            <label>새 파일 첨부:</label>
            <input type="file" name="newFiles" multiple><br/><br/>

            <button type="submit">수정</button>
            <button type="button"
                    onclick="location.href = '${pageContext.request.contextPath}/board/list'">취소</button>
        </form>

    </body>
</html>

