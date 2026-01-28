<%-- 
    Document   : list_body
    Created on : 2026. 1. 19., 오전 4:29:09
    Author     : Haruki
--%>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h2>📋 게시글 목록</h2>

<div class="write-actions">
    <a class="btn-write"
       href="${pageContext.request.contextPath}/board/writeForm">
        ✏️ 새 글 작성
    </a>
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
            <tr class="${post.id eq currentPostId ? 'active' : ''}">
                <!--onclick="location.href = '${rowUrl}'">   현재글이면 active 표시 -->

                <td>
                    ${postPage.totalElements
                      - (postPage.number * postPage.size)
                      - status.index}
                </td>

                <td class="title">
                    <a href="${rowUrl}" class="row-link">
                        ${post.title}
                    </a>
                </td>
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

        // JSP → JS 키워드 전달
        var keyword = '<c:out value="${keyword}" />'.trim();
        var searchType = '<c:out value="${searchType}" />'.trim();
        if (!keyword || !searchType)
            return;

        // 정규식 특수문자 escape
        function escapeRegExp(str) {
            return str.replace(/[-\/\\^$*+?.()|[\]{}]/g, '\\$&');
        }

        // 검색기준 분기 (하이라이트 처리용)
        var selector = '';
        if (searchType === 'author') {
            selector = '.author';
        } else {
            selector = '.row-link';
        }
        var regex = new RegExp('(' + escapeRegExp(keyword) + ')', 'gi'); // 전부, 대소문자 구분 X

        // 하이라이트
        document.querySelectorAll(selector).forEach(function (el) {
            el.innerHTML = el.textContent.replace(
                    regex,
                    '<span class="highlight">$1</span>'
                    );
        });
    });
</script>



