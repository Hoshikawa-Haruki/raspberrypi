<%-- 
    Document   : write
    Created on : 2025. 10. 13., 오후 1:40:53
    Author     : Haruki
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>짭케 마이너 갤러리</title>
        <link rel="stylesheet" type="text/css"
              href="${pageContext.request.contextPath}/css/write.css">
    </head>
    <body>
        <div class="container">
            <h2>✏️ 새 글 작성</h2>

            <form method="post" enctype="multipart/form-data"
                  action="${pageContext.request.contextPath}/board/save">
                <label>작성자:</label>
                <input type="text" name="author" value="라붕이" maxlength="10"
                       oninput="this.value = this.value.replace(/\s/g, '');" required><br/><br/>

                <label>비밀번호:</label>
                <input type="password" name="password" value="0000" maxlength="8"
                       oninput="this.value = this.value.replace(/\s/g, '');" required><br/><br/>

                <label>제목:</label>
                <input type="text" name="title" maxlength="40" required><br/><br/>

                <label>내용:</label><br/>
                <textarea name="content" rows="10" cols="60" 
                          placeholder="작성자와 비밀번호는 기본값으로 라붕이, 0000 으로 설정됩니다.
(수정가능)"
                          required></textarea><br/><br/>

                <label>첨부파일:</label>
                <input type="file" name="files" multiple><br/><br/>

                <button type="submit">등록</button>
                <button type="button" 
                        onclick="location.href = '${pageContext.request.contextPath}/board/list'">취소</button>
            </form>
        </div>
    </body>
</html>

