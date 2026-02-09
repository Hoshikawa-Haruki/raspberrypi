<%-- 
    Document   : myPage
    Created on : 2026. 1. 12., 오후 2:13:25
    Author     : Haruki
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <title>마이페이지</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/mypage.css">
    </head>

    <body>

        <jsp:include page="/WEB-INF/views/board/top_common_menu.jsp" />

        <div class="mypage-wrapper">
            <div class="mypage-container">

                <!-- 좌측 -->
                <aside class="mypage-left">
                    <div class="profile-box">
                        <div class="avatar"></div>
                        <div class="username">${user.nickname}</div>
                        <div class="email">${user.email}</div>
                    </div>

                    <ul class="mypage-menu">
                        <li class="active">프로필 관리</li>
                        <li>보안 설정</li>
                        <li>이력 관리</li>
                        <li class="danger">
                            <a href="/member/withdraw">회원탈퇴</a></li>

                    </ul>
                </aside>

                <!-- 우측 -->
                <section class="mypage-right">

                    <div class="card">
                        <div class="card-header">내 프로필</div>
                        <div class="card-body">
                            <table class="info-table">
                                <tr>
                                    <th>닉네임</th>
                                    <td>${user.nickname}</td>
                                </tr>
                                <tr>
                                    <th>이메일</th>
                                    <td>${user.email}</td>
                                </tr>
                                <tr>
                                    <th>가입일</th>
                                    <td>${user.createdAt.toLocalDate()}</td>
                                </tr>
                            </table>
                        </div>
                    </div>

                    <div class="card">
                        <div class="card-header">
                            내 작성글
                            <a href="${pageContext.request.contextPath}/board/list?searchType=writer&keyword=${user.nickname}"
                               class="mypost-link">
                                [<span id="my-post-count">0</span>]
                            </a>
                        </div>
                        <div class="card-body">
                            <ul class="my-post-list" id="my-post-list"></ul>
                            <p id="my-post-empty" class="empty" style="display:none;">
                                등록된 게시글이 없습니다.
                            </p>
                            <div class="pagination" id="my-post-pagination"></div>
                        </div>
                    </div>

                </section>

            </div>
        </div>

    </body>
</html>
<script src="${pageContext.request.contextPath}/js/board/myPage.js"></script>
