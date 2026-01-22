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
                        <li><a href="/mypage">프로필 관리</a></li>
                        <li>보안 설정</li>
                        <li>이력 관리</li>
                        <li class="danger active">회원탈퇴</li>
                    </ul>
                </aside>

                <!-- 우측 -->
                <section class="mypage-right">

                    <div class="card">
                        <div class="card-header">회원 탈퇴</div>
                        <div class="card-body">

                            <p class="text-danger fw-bold">
                                회원 탈퇴 시 아래 정보가 모두 삭제되며 복구할 수 없습니다.
                            </p>

                            <ul class="text-muted">
                                <li>작성한 게시글 및 댓글</li>
                                <li>계정 정보 (이메일, 닉네임)</li>
                                <li>활동 이력</li>
                            </ul>

                            <hr>

                            <form method="post" action="${pageContext.request.contextPath}/member/withdraw">

                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

                                <!-- 비밀번호 확인 -->
                                <div class="mb-3">
                                    <label class="form-label">비밀번호 확인</label>
                                    <input type="password" name="password" class="form-control" required>
                                </div>

                                <!-- 동의 체크 -->
                                <div class="form-check mb-4">
                                    <input class="form-check-input" type="checkbox" id="agreeCheck" required>
                                    <label class="form-check-label" for="agreeCheck">
                                        위 내용을 모두 확인했으며, 탈퇴에 동의합니다.
                                    </label>
                                </div>

                                <!-- 버튼 -->
                                <div class="text-end">
                                    <a href="${pageContext.request.contextPath}/mypage"
                                       class="btn btn-outline-secondary">
                                        취소
                                    </a>

                                    <button type="submit"
                                            class="btn btn-danger"
                                            onclick="return confirm('정말 회원 탈퇴하시겠습니까?');">
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

