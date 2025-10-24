<%-- 
    Document   : list
    Created on : 2025. 10. 13., 오후 1:46:49
    Author     : Haruki
--%>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
    <head>
        <title>짭케 마이너 갤러리</title>
    </head>
    <body>
        <h2>게시글 목록</h2>

        <a href="${pageContext.request.contextPath}/board/write">✏️ 새 글 작성</a>
        <br/><br/>

        <table border="1" cellpadding="8" cellspacing="0">
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
                    <tr>
                        <td>${postList.size() - status.index}</td> <!-- 역순 번호 -->
                        <td>
                            <a href="${pageContext.request.contextPath}/board/view/${post.id}">
                                <c:out value="${post.title}"/>
                            </a>
                        </td>
                        <td>${post.author} (${post.maskedIp})</td>
                        <td>${post.formattedCreatedAt}</td>
                    </tr>
                </c:forEach>

            </tbody>
        </table>

        <c:if test="${empty postList}">
            <p>등록된 게시글이 없습니다.</p>
        </c:if>

    </body>
</html>

