<%-- 
    Document   : signup
    Created on : 2025. 10. 29., 오전 12:53:30
    Author     : Haruki
--%>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <title>회원가입</title>
        <jsp:include page="/WEB-INF/views/board/head.jsp" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/member.css">
    </head>
    <body>
        <jsp:include page="/WEB-INF/views/board/top_common_menu.jsp" />
        <div class="login-wrapper">
            <!-- 왼쪽 폼 -->
            <div class="login-form">
                <h2>📝 회원가입</h2>
                <form method="post" action="${pageContext.request.contextPath}/member/signup">
                    <input id="email" type="email" name="email" placeholder="이메일" required />
                    <div id="emailMsg" class="error-msg"></div>
                    <input id="password" type="password" name="password" placeholder="비밀번호" minlength="4" maxlength="20" required />
                    <input id="confirmPassword" type="password" name="confirmPassword" placeholder="비밀번호 확인" minlength="4" required />
                    <div id="passwordMsg" class="error-msg"></div>
                    <input type="text" name="nickname" placeholder="닉네임" minlength="2" maxlength="10" required />
                    <input type="hidden" name="_csrf" value="${_csrf.token}">

                    <c:if test="${not empty errorMessage}">
                        <div class="form-error">
                            ${errorMessage}
                        </div>
                    </c:if>
                    <button type="submit">가입하기</button>

                    <div class="links">
                        <a href="${pageContext.request.contextPath}/member/loginForm">로그인으로 이동</a>
                    </div>
                </form>
            </div>

            <!-- 오른쪽 배너 -->
            <div class="login-banner">
                <img src="${pageContext.request.contextPath}/images/crown_signup.png" alt="회원가입 배너" />
            </div>
        </div>
    </body>

    <script>
        const emailInput = document.getElementById("email");
        const msg = document.getElementById("emailMsg");

        emailInput.addEventListener("blur", function () {

            const email = emailInput.value;

            if (!email)
                return;

            fetch("/api/member/check-email?email=" + encodeURIComponent(email))
                    .then(res => res.json())
                    .then(exists => {
                        if (exists) {
                            msg.innerText = "이미 사용 중인 이메일입니다.";
                            msg.style.color = "red";
                        } else {
                            msg.innerText = "사용 가능한 이메일입니다.";
                            msg.style.color = "green";
                        }

                    });
        });
    </script>

    <script>
        const password = document.getElementById("password");
        const confirmPassword = document.getElementById("confirmPassword");
        const passwordMsg = document.getElementById("passwordMsg");

        function checkPasswordMatch() {

            const pw = password.value;
            const confirmPw = confirmPassword.value;

            // 비밀번호 길이 체크
            if (pw.length > 0 && pw.length < 4) {
                passwordMsg.innerText = "비밀번호는 최소 4자 이상이어야 합니다.";
                passwordMsg.style.color = "red";
                return;
            }

            if (pw.length > 20) {
                passwordMsg.innerText = "비밀번호는 최대 20자까지 가능합니다.";
                passwordMsg.style.color = "red";
                return;
            }

            // confirm 입력 안 했으면 메시지 숨김
            if (!confirmPw) {
                passwordMsg.innerText = "";
                return;
            }

            // 비밀번호 일치 체크
            if (pw === confirmPw) {
                passwordMsg.innerText = "비밀번호가 일치합니다.";
                passwordMsg.style.color = "green";
            } else {
                passwordMsg.innerText = "비밀번호가 일치하지 않습니다.";
                passwordMsg.style.color = "red";
            }
        }

        password.addEventListener("input", checkPasswordMatch);
        confirmPassword.addEventListener("input", checkPasswordMatch);
    </script>

</html>

