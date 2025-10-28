<%-- 
    Document   : signup
    Created on : 2025. 10. 29., 오전 12:53:30
    Author     : Haruki
--%>

<%@ page contentType="text/html; charset=UTF-8" %>
<html>
    <head>
        <title>회원가입</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/member.css">
    </head>
    <body>

        <div class="login-wrapper">

            <!-- 왼쪽 폼 -->
            <div class="login-form">
                <h2>📝 회원가입</h2>
                <form method="post" action="${pageContext.request.contextPath}/member/signup">
                    <input type="email" name="email" placeholder="이메일" required />
                    <input type="password" name="password" placeholder="비밀번호" required />
                    <input type="text" name="nickname" placeholder="닉네임" required />

                    <button type="submit">가입하기</button>

                    <div class="links">
                        <a href="${pageContext.request.contextPath}/member/login">로그인으로 이동</a>
                    </div>
                </form>
            </div>

            <!-- 오른쪽 배너 -->
            <div class="login-banner">
                <img src="${pageContext.request.contextPath}/images/signup_banner.png" alt="회원가입 배너" />
            </div>

        </div>

    </body>
</html>

