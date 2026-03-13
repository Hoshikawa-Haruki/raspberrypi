<%-- 
    Document   : list_body
    Created on : 2026. 1. 19., 오전 4:29:09
    Author     : Haruki
--%>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="board-header">
    <h2 class="board-title">📋 게시글 목록</h2>

    <div class="write-actions">
        <a class="btn btn-write"
           href="${pageContext.request.contextPath}/board/writeForm">
            ✏️ 새 글 작성
        </a>
    </div>
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
                <c:url var="rowUrl" value="/board/view/${post.id}">

                    <!-- 검색 중일 때만 검색 파라미터 유지 -->
                    <c:if test="${not empty searchType and not empty keyword}">
                        <c:param name="searchType" value="${searchType}" />
                        <c:param name="keyword" value="${keyword}" />
                    </c:if>

                    <!-- 목록 조회시 page 유지 -->
                    <c:if test="${not empty pageInfo}">
                        <c:param name="page" value="${pageInfo.currentPage}" />
                    </c:if>

                </c:url>

                <tr class="${post.id eq currentPostId ? 'active' : ''}">

                    <td>
                        ${post.displayNo}
                    </td>

                    <td class="title">
                        <a href="${pageContext.request.contextPath}${rowUrl}" class="row-link">
                            ${post.title}
                            <c:if test="${post.commentCount > 0}">
                                <span class="row-comment-count">[${post.commentCount}]</span>
                            </c:if>
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



