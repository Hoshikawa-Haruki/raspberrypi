<%-- 
    Document   : write
    Created on : 2025. 10. 13., 오후 1:40:53
    Author     : Haruki
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>글 작성</title>
    </head>
    <body>
        <h2>✏️ 새 글 작성</h2>

        <form method="post" enctype="multipart/form-data"
              action="${pageContext.request.contextPath}/board/save">
            <label>작성자:</label>
            <input type="text" name="writer" required><br/><br/>

            <label>비밀번호:</label>
            <input type="password" name="password" required><br/><br/>

            <label>제목:</label>
            <input type="text" name="title" required><br/><br/>

            <label>내용:</label><br/>
            <textarea name="content" rows="10" cols="60" required></textarea><br/><br/>

            <label>첨부파일:</label>
            <input type="file" name="files" multiple><br/><br/>

            <button type="submit">등록</button>
            <a href="/board/list">취소</a>
        </form>
    </body>
</html>

