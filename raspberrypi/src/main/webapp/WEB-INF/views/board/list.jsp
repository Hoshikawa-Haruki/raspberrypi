<%-- 
    Document   : list
    Created on : 2025. 10. 13., 오후 1:46:49
    Author     : Haruki
--%>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
    <head>
        <title>짭케 마이너 갤러리</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
        <link rel="stylesheet" type="text/css"
              href="${pageContext.request.contextPath}/css/list.css">
        <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/rappi_favicon.png">
    </head> 
    <body>

        <div class="container">
            <h2>📋 게시글 목록</h2>

            <div class="actions">
                <sec:authorize access="isAnonymous()">
                    <button onclick="location.href = '${pageContext.request.contextPath}/member/loginForm'">로그인</button>
                    <button onclick="location.href = '${pageContext.request.contextPath}/member/signupForm'">회원가입</button>
                    <button onclick="location.href = '${pageContext.request.contextPath}/board/writeForm'">✏️ 새 글 작성</button>
                </sec:authorize>
                <sec:authorize access="isAuthenticated()">
                    <form method="post" action="${pageContext.request.contextPath}/logout" style="display:inline;">
                        <input type="hidden" name="_csrf" value="${_csrf.token}">
                        <button type="submit">로그아웃</button>
                    </form>
                    <button onclick="location.href = '${pageContext.request.contextPath}/board/writeForm'">✏️ 새 글 작성</button>
                </sec:authorize>
                
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
                            <td>${post.authorNameSnapshot} (${post.maskedIp})</td>
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
