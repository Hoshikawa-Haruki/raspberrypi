<%-- 
    Document   : login
    Created on : 2025. 10. 29., 오전 12:53:11
    Author     : Haruki
--%>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <title>로그인</title>
        <jsp:include page="/WEB-INF/views/board/head.jsp" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/member.css">
    </head>
    <body>
        <jsp:include page="/WEB-INF/views/board/top_common_menu.jsp" />
        <div class="login-wrapper">

            <!-- 왼쪽 폼 -->
            <div class="login-form">
                <h2>🔐 로그인</h2>
                <fieldset class="login-fieldset">
                    <form method="post" action="${pageContext.request.contextPath}/login">
                        <input type="email" name="email" value="${param.email}" placeholder="이메일" required />
                        <input type="password" name="password" placeholder="비밀번호" required />
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        <button type="submit">로그인</button>

                        <div class="form-error">
                            <c:if test="${not empty sessionScope.loginErrorMessage}">
                                ${sessionScope.loginErrorMessage}
                            </c:if>
                        </div>

                        <c:remove var="loginErrorMessage" scope="session"/>

                        <div class="options">
                            <label>
                                <input type="checkbox" name="remember-me"> 로그인 상태 유지
                            </label>
                        </div>

                        <div class="links">
                            <a href="${pageContext.request.contextPath}/member/signupForm">회원가입</a> |
                            <a href="#">아이디 찾기</a> |
                            <a href="#">비밀번호 찾기</a>
                        </div>
                        <div class = "links"><!-- comment -->
                            <a href="${pageContext.request.contextPath}/">메인화면으로</a>
                        </div>
                    </form>
                </fieldset>
            </div>

            <!-- 오른쪽 배너 -->
            <div class="login-banner">
                <img src="${pageContext.request.contextPath}/images/redhood_login.png" alt="로그인 배너" />
            </div>
        </div>
        <c:if test="${param.signupSuccess ne null}">
            <script>
                alert("회원가입이 완료되었습니다.");
            </script>
        </c:if>
    </body>
</html>