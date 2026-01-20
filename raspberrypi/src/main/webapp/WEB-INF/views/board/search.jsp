<%-- 
    Document   : search
    Created on : 2026. 1. 17., 오후 6:24:50
    Author     : Haruki

    검색창 UI
--%>

<%-- 검색창 UI --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form method="get"
      action="${pageContext.request.contextPath}/board/list"
      class="search-form">

    <!-- 서버로 넘어가는 검색 타입 -->
    <input type="hidden" name="searchType" id="searchType"
           value="${empty searchType ? 'title_content' : searchType}">

    <!-- 드롭다운 -->
    <div class="dropdown">
        <button type="button" class="dropdown-btn" id="dropdownBtn">
            <span id="dropdownLabel">
                <c:choose>
                    <c:when test="${searchType eq 'title'}">제목</c:when>
                    <c:when test="${searchType eq 'content'}">내용</c:when>
                    <c:when test="${searchType eq 'writer'}">글쓴이</c:when>
                    <c:when test="${searchType eq 'comment'}">댓글</c:when>
                    <c:otherwise>제목+내용</c:otherwise>
                </c:choose>
            </span>
            <span class="arrow">▼</span>
        </button>

        <ul class="dropdown-menu" id="dropdownMenu">
            <li data-type="title_content">제목+내용</li>
            <li data-type="title">제목</li>
            <li data-type="content">내용</li>
            <li data-type="writer">글쓴이</li>
            <li data-type="comment">댓글</li>
        </ul>
    </div>

    <!-- 검색창 -->
    <div class="search-group">
        <input type="text"
               name="keyword"
               value="${keyword}"
               class="search-input"
               placeholder="검색어를 입력하세요"
               required>

        <button type="submit" class="search-btn">🔍</button>
    </div>
</form>

<script>
    const btn = document.getElementById("dropdownBtn");
    const menu = document.getElementById("dropdownMenu");
    const label = document.getElementById("dropdownLabel");
    const hidden = document.getElementById("searchType");

    btn.addEventListener("click", () => {
        menu.style.display = menu.style.display === "block" ? "none" : "block";
    });

    menu.querySelectorAll("li").forEach(item => {
        item.addEventListener("click", () => {
                hidden.value = item.dataset.type; // 검색조건 결정
            label.textContent = item.textContent;
            menu.style.display = "none";
        });
    });

    document.addEventListener("click", (e) => {
        if (!btn.contains(e.target) && !menu.contains(e.target)) {
            menu.style.display = "none";
        }
    });
</script>



