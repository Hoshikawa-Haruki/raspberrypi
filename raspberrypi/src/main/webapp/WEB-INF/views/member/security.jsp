<%-- 
    Document   : security
    Created on : 2026. 3. 19., 오전 3:01:12
    Author     : Haruki
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <title>보안 설정</title>
        <meta name="_csrf" content="${_csrf.token}">
        <meta name="_csrf_header" content="${_csrf.headerName}">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/mypage.css">
    </head>

    <body>

        <jsp:include page="/WEB-INF/views/board/top_common_menu.jsp" />

        <div class="mypage-wrapper">
            <div class="mypage-container">

                <!-- 좌측 그대로 유지 -->
                <aside class="mypage-left">
                    <div class="profile-box">
                        <div class="avatar"></div>
                        <div class="username">${user.nickname}</div>
                        <div class="email">${user.email}</div>
                    </div>

                    <ul class="mypage-menu">
                        <li>
                            <a href="/member/mypage">프로필 관리</a>
                        </li>
                        <li class="active">
                            <a href="/member/security">보안 설정</a>
                        </li>
                        <li class="danger">
                            <a href="/member/withdrawForm">회원탈퇴</a>
                        </li>
                    </ul>
                </aside>

                <!-- 우측 -->
                <section class="mypage-right">

                    <div class="card">
                        <div class="card-header">비밀번호 변경</div>

                        <div class="card-body">
                            <div class="desc-box">
                                <p class="security-desc">
                                    안전한 비밀번호로 내 정보를 보호하세요
                                </p>

                                <ul class="security-tiplist">
                                    <li>다른 아이디/사이트에서 사용한 적 없는 비밀번호</li>
                                    <li>이전에 사용한 적 없는 비밀번호가 안전합니다.</li>
                                </ul>
                            </div>



                            <form action="/member/change-password" method="post" class="security-form">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

                                <input type="password" id="currentPassword" name="currentPassword" placeholder="현재 비밀번호" required>


                                <input type="password" id="newPassword" name="newPassword" placeholder="새 비밀번호" required>

                                <input type="password" id="confirmPassword" name="confirmPassword" placeholder="새 비밀번호 확인" required>
                                <c:if test="${not empty error}">
                                    <div class="form-error">
                                        ${error}
                                    </div>
                                </c:if>
                                <div id="confirmPasswordMsg" class="error-msg"></div>


                                <button type="submit" class="btn-submit">확인</button>
                                <button type="button" class="btn-cancel" onclick="history.back()">취소</button>
                            </form>
                        </div>
                    </div>

                </section>

            </div>
        </div>
        <c:if test="${param.success eq 'true'}">
            <script>
                window.addEventListener("DOMContentLoaded", () => {
                    alert("비밀번호가 성공적으로 변경되었습니다.");
                });
            </script>
        </c:if>
    </body>

    <script>
        document.querySelector(".security-form").addEventListener("submit", function (e) {

            const newPw = document.getElementById("newPassword");
            const confirmPw = document.getElementById("confirmPassword");

            const confirmMsg = document.getElementById("confirmPasswordMsg");

            // 서버 에러 초기화
            const globalError = document.querySelector(".form-error");
            if (globalError) {
                globalError.remove();
            }

            // 프론트 에러 초기화
            confirmMsg.textContent = "";

            let valid = true;

            if (newPw.value !== confirmPw.value) {
                confirmMsg.textContent = "새 비밀번호가 일치하지 않습니다.";
                valid = false;
            }

            if (!valid) {
                e.preventDefault();
            }
        });
    </script>
</html>
