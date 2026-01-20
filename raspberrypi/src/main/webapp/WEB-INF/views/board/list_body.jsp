<%-- 
    Document   : list_body
    Created on : 2026. 1. 19., 오전 4:29:09
    Author     : Haruki
--%>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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

            <!-- URL 결정 (검색 시 or 전체 목록 조회 시) -->
            <c:choose>
                <c:when test="${not empty keyword and not empty searchType}">
                    <c:set var="rowUrl"
                           value="${pageContext.request.contextPath}/board/view/${post.id}?searchType=${searchType}&keyword=${keyword}&page=${postPage.number}" />
                </c:when>
                <c:otherwise>
                    <c:set var="rowUrl"
                           value="${pageContext.request.contextPath}/board/view/${post.id}?page=${postPage.number}" />
                </c:otherwise>
            </c:choose>

            <tr onclick="location.href = '${rowUrl}'">

                <td>
                    ${postPage.totalElements
                      - (postPage.number * postPage.size)
                      - status.index}
                </td>

                <td class="title">${post.title}</td>
                <td class="author">${post.authorNameSnapshot} (${post.maskedIp})</td>
                <td>${post.formattedCreatedAt}</td>

            </tr>
        </c:forEach>
    </tbody>
</table>

<c:if test="${empty postList}">
    <p class="empty">등록된 게시글이 없습니다.</p>
</c:if>

<script>
    document.addEventListener('DOMContentLoaded', function () {

        // JSP → JS 안전하게 값 전달
        var keyword = '<c:out value="${keyword}" />'.trim();
        if (!keyword)
            return;

        // 정규식 특수문자 escape (JSP EL 충돌 없음)
        function escapeRegExp(str) {
            return str.replace(/[-\/\\^$*+?.()|[\]{}]/g, '\\$&');
        }

        var regex = new RegExp('(' + escapeRegExp(keyword) + ')', 'gi');

        document.querySelectorAll('.title, .author').forEach(function (el) {
            el.innerHTML = el.textContent.replace(
                    regex,
                    '<span class="highlight">$1</span>'
                    );
        });
    });
</script>



