<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
    <head>
        <meta charset="UTF-8">
        <title>${post.title}</title>
        <style>
            body {
                font-family: "Noto Sans KR", sans-serif;
                background-color: #f8f9fa;
                margin: 0;
                padding: 0;
            }

            .container {
                width: 900px;
                margin: 40px auto;
                background-color: #fff;
                border: 1px solid #ddd;
                border-radius: 6px;
                box-shadow: 0 2px 5px rgba(0,0,0,0.05);
                padding: 25px 35px;
            }

            .post-header {
                border-bottom: 1px solid #e5e5e5;
                padding-bottom: 10px;
                margin-bottom: 20px;
            }

            .post-title {
                font-size: 22px;
                font-weight: bold;
                color: #333;
                margin-bottom: 10px;
            }

            .post-info {
                color: #777;
                font-size: 14px;
            }

            .post-info span {
                margin-right: 15px;
            }

            .post-content {
                font-size: 16px;
                line-height: 1.7;
                color: #333;
                min-height: 200px;
                margin-bottom: 30px;
                white-space: pre-line;
            }

            .attachments img {
                max-width: 100%;
                margin: 10px 0;
                border-radius: 4px;
            }

            .post-footer {
                display: flex;
                justify-content: space-between;
                margin-top: 30px;
                padding-top: 20px;
                border-top: 1px solid #ddd;
            }

            .btn {
                display: inline-block;
                padding: 7px 15px;
                border: 1px solid #ccc;
                border-radius: 4px;
                background-color: #f8f9fa;
                color: #333;
                text-decoration: none;
                font-size: 14px;
            }

            .btn:hover {
                background-color: #e9ecef;
            }

            .btn-danger {
                border-color: #dc3545;
                color: #dc3545;
            }

            .btn-danger:hover {
                background-color: #dc3545;
                color: #fff;
            }
        </style>
    </head>
    <body>
        <div class="container">

            <!-- 게시글 상단 -->
            <div class="post-header">
                <div class="post-title">${post.title}</div>
                <div class="post-info">
                    <span>작성자: ${post.author}</span>
                    <span>작성일: ${post.formattedCreatedAt}</span>
                </div>
            </div>

            <!-- 본문 -->
            <div class="post-content">
                ${post.content}
            </div>

            <!-- 첨부파일 -->
            <c:if test="${not empty post.attachments}">
                <div class="attachments">
                    <c:forEach var="file" items="${post.attachments}">
                        <img src="/uploads/${file.savedName}" alt="첨부파일">
                    </c:forEach>
                </div>
            </c:if>

            <!-- 하단 버튼 -->
            <div class="post-footer">
                <div>
                    <a href="/board/list" class="btn">목록</a>
                </div>
                <div>
                    <a href="/board/edit/${post.id}" class="btn">수정</a>
                    <form method="post" action="/board/delete/${post.id}" style="display:inline;">
                        <button type="submit" class="btn btn-danger">삭제</button>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
