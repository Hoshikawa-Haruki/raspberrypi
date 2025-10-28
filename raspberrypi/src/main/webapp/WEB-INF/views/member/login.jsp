<%-- 
    Document   : login
    Created on : 2025. 10. 29., 오전 12:53:11
    Author     : Haruki
--%>

<%@ page contentType="text/html; charset=UTF-8" %>
<html>
    <head>
        <title>로그인</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/member.css">
    </head>
    <body>

        <div class="login-wrapper">

            <!-- 왼쪽 폼 -->
            <div class="login-form">
                <h2>🔐 로그인</h2>
                <form method="post" action="${pageContext.request.contextPath}/member/login">
                    <input type="email" name="email" placeholder="이메일" required />
                    <input type="password" name="password" placeholder="비밀번호" required />

                    <button type="submit">로그인</button>

                    <div class="options">
                        <label><input type="checkbox" name="remember"> 로그인 상태 유지</label>
                        <a href="#">비밀번호 찾기</a>
                    </div>

                    <div class="links">
                        <a href="${pageContext.request.contextPath}/member/signup">회원가입</a> |
                        <a href="#">아이디 찾기</a>
                    </div>
                </form>
            </div>

            <!-- 오른쪽 배너 -->
            <div class="login-banner">
                <img src="${pageContext.request.contextPath}/images/login_banner.png" alt="로그인 배너" />
            </div>

        </div>

    </body>
</html>


