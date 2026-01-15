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
                        <li class="danger">회원 탈퇴</li>
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
                                    <td>${createdDate}</td>
                                </tr>
                            </table>
                        </div>
                    </div>

                    <div class="card">
                        <div class="card-header">내 작성글</div>
                        <div class="card-body">
                            <c:choose>
                                <c:when test="${empty postPage.content}">
                                    <p>작성한 글이 없습니다.</p>
                                </c:when>
                                <c:otherwise>
                                    <ul class="my-post-list">
                                        <c:forEach var="post" items="${postPage.content}">
                                            <li>
                                                <a href="${pageContext.request.contextPath}/board/view/${post.id}" class="post-title">
                                                    ${post.title}
                                                </a>
                                                <span class="post-date">${post.formattedCreatedAt}</span>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </c:otherwise>
                            </c:choose>

                            <!-- 페이지네이션 -->
                            <c:if test="${postPage.totalPages > 1}">
                                <div class="pagination">
                                    <c:if test="${!postPage.first}">
                                        <a href="?page=${postPage.number - 1}">이전</a>
                                    </c:if>

                                    <span>${postPage.number + 1} / ${postPage.totalPages}</span>

                                    <c:if test="${!postPage.last}">
                                        <a href="?page=${postPage.number + 1}">다음</a>
                                    </c:if>
                                </div>
                            </c:if>
                        </div>
                    </div>

                </section>

            </div>
        </div>

    </body>
</html>

