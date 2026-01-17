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
        <jsp:include page="/WEB-INF/views/board/head.jsp" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
        <link rel="stylesheet" type="text/css"
              href="${pageContext.request.contextPath}/css/list.css">
    </head>

    <body>
        <jsp:include page="/WEB-INF/views/board/top_common_menu.jsp" />

        <div class="container">
            <h2>📋 게시글 목록</h2>

            <div class="actions">
                <button onclick="location.href = '${pageContext.request.contextPath}/board/writeForm'">✏️ 새 글 작성</button>
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
                            <td>
                                ${postPage.totalElements
                                  - (postPage.number * postPage.size)
                                  - status.index}
                            </td>
                            <td class="title">${post.title}</td>
                            <td>${post.authorNameSnapshot} (${post.maskedIp})</td>
                            <td>${post.formattedCreatedAt}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <div class="pagination">
                
                <!-- 맨 처음으로 -->
                <c:if test="${currentPage > 0}">
                    <a class="nav" href="?page=0">
                        ◀
                    </a>
                </c:if>

                <!-- 이전 블록 -->
                <c:if test="${currentPage > 0}">
                    <a class="nav"
                       href="?page=${currentPage - 1}">
                        이전
                    </a>
                </c:if>

                <!-- 페이지 번호 -->
                <c:forEach var="i" begin="${startPage}" end="${endPage}">
                    <c:choose>
                        <c:when test="${i == currentPage}">
                            <span class="page active">${i + 1}</span>
                        </c:when>
                        <c:otherwise>
                            <a class="page"
                               href="?page=${i}">
                                ${i + 1}
                            </a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <!-- 다음 블록 -->
                <c:if test="${currentPage < totalPages - 1}">
                    <a class="nav"
                       href="?page=${currentPage + 1}">
                        다음
                    </a>
                </c:if>

                <!-- 맨 끝으로 -->
                <c:if test="${currentPage < totalPages - 1}">
                    <a class="nav"
                       href="?page=${totalPages - 1}">
                        ▶
                    </a>
                </c:if>

            </div>

            <c:if test="${empty postList}">
                <p class="empty">등록된 게시글이 없습니다.</p>
            </c:if>
                
        </div>

    </body>
    <jsp:include page="/WEB-INF/views/board/footer.jsp"/>
</html>
