<%-- 
    Document   : withdraw
    Created on : 2026. 1. 22., 오후 10:02:42
    Author     : Haruki
--%>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <title>회원 탈퇴</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/mypage.css">
    </head>

    <body>

        <jsp:include page="/WEB-INF/views/board/top_common_menu.jsp" />

        <div class="mypage-wrapper">
            <div class="mypage-container">

                <!-- 좌측 (완전히 동일) -->
                <aside class="mypage-left">
                    <div class="profile-box">
                        <div class="avatar"></div>
                        <div class="username">${user.nickname}</div>
                        <div class="email">${user.email}</div>
                    </div>

                    <ul class="mypage-menu">
                        <li>
                            <a href="/member/withdrawForm">프로필 관리</a>
                        </li>
                        <li>
                            <a href="/member/withdrawForm">보안 설정</a>
                        </li>
                        <li class="danger">
                            <a href="/member/withdrawForm">회원탈퇴</a>
                        </li>
                    </ul>
                </aside>

                <!-- 우측 -->
                <section class="mypage-right">

                    <div class="card">
                        <div class="card-header danger-header">회원 탈퇴</div>

                        <div class="card-body">

                            <!-- 경고 박스 -->
                            <div class="danger-box">
                                <div class="danger-title">
                                    ⚠ 회원 탈퇴 시 계정은 비활성화되며, 로그인 및 서비스 이용이 제한됩니다.
                                </div>

                                <ul>
                                    <li>계정 정보 (이메일, 비밀번호, 닉네임)는 더 이상 사용되지 않습니다</li>
                                    <li>작성한 게시글 및 댓글은 서비스 기록 보존을 위해 유지될 수 있습니다</li>
                                    <li>활동 이력은 일부 보존될 수 있습니다</li>
                                </ul>

                                <hr>

                                <p class="danger-note">
                                    이 작업은 되돌릴 수 없습니다.
                                </p>
                            </div>

                            <form method="post" action="${pageContext.request.contextPath}/member/withdraw">

                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

                                <!-- 비밀번호 확인 -->
                                <div class="form-group">
                                    <label>비밀번호 확인</label>
                                    <input type="password" name="password" class="form-control" required>

                                    <c:if test="${not empty error}">
                                        <div class="error-msg">${error}</div>
                                    </c:if>
                                </div>



                                <!-- 동의 -->
                                <div class="agree-box">
                                    <input type="checkbox" id="agreeCheck" required>
                                    <label for="agreeCheck">
                                        위 내용을 모두 확인했으며 탈퇴에 동의합니다.
                                    </label>
                                </div>

                                <!-- 버튼 -->
                                <div class="withdraw-actions">
                                    <a href="${pageContext.request.contextPath}/mypage"
                                       class="btn btn-cancel">
                                        취소
                                    </a>

                                    <button type="submit"
                                            class="btn btn-danger"
                                            onclick="return confirm('정말 회원 탈퇴하시겠습니까?\n이 작업은 되돌릴 수 없습니다.');">
                                        회원 탈퇴
                                    </button>
                                </div>

                            </form>

                        </div>
                    </div>
                </section>

            </div>
        </div>
    </body>
</html>

