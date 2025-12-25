<%-- 
    Document   : top_common_menu
    Created on : 2025. 12. 22., 오후 8:48:52
    Author     : Haruki
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/top_menu.css">
</head>

<div class="top-user-menu">
    <sec:authorize access="isAnonymous()">
        <a href="${pageContext.request.contextPath}/member/loginForm" class="top-btn">
            로그인
        </a>
        <a href="${pageContext.request.contextPath}/member/signupForm" class="top-btn">
            회원가입
        </a>
    </sec:authorize>

    <sec:authorize access="isAuthenticated()">
        <span class="user-greeting">
            <strong>
                <sec:authentication property="principal.member.nickname"/>
            </strong> 님
        </span>
        <form method="post"
              action="${pageContext.request.contextPath}/logout"
              style="display:inline;">
            <input type="hidden"
                   name="${_csrf.parameterName}"
                   value="${_csrf.token}">
            <button type="submit" class="top-btn">로그아웃</button>
        </form>
    </sec:authorize>
</div>

<!-- 네비게이션 -->
<nav class="top-nav">
    <div class="top-inner">
        <ul class="nav-list">
            <li><a href="/">홈</a></li>
            <li><a href="/profile">프로필</a></li>
            <li><a href="/board/list">게시판</a></li>
            <li><a href="/portfolio">포트폴리오</a></li>
        </ul>
    </div>
</nav>      


