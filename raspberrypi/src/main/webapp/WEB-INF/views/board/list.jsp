<%-- 
    Document   : list
    Created on : 2025. 10. 13., 오후 1:46:49
    Author     : Haruki
--%>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title>짭케 마이너 갤러리</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
        <link rel="stylesheet" type="text/css"
              href="${pageContext.request.contextPath}/css/list.css">
    </head>
    <body>

        <div class="container">
            <h2>📋 게시글 목록</h2>

            <div class="actions">
                <button onclick="location.href = '${pageContext.request.contextPath}/member/login'">로그인</button>
                <button onclick="location.href = '${pageContext.request.contextPath}/member/signup'">회원가입</button>
                <button onclick="location.href = '${pageContext.request.contextPath}/board/write'">✏️ 새 글 작성</button>
            </div>

            <table class="board-table">
                <thead>
                    <tr>
                        <th>번호</th>
                        <th>제목</th>
                        <th>작성자</th>
                        <th>작성일</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="post" items="${postList}" varStatus="status">
                        <tr onclick="location.href = '${pageContext.request.contextPath}/board/view/${post.id}'">
                            <td>${postList.size() - status.index}</td>
                            <td class="title"><c:out value="${post.title}"/></td>
                            <td>${post.author} (${post.maskedIp})</td>
                            <td>${post.formattedCreatedAt}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <c:if test="${empty postList}">
                <p class="empty">등록된 게시글이 없습니다.</p>
            </c:if>

        </div>

    </body>
</html>
