<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <title>관리자 v2</title>
        <jsp:include page="/WEB-INF/views/board/head.jsp" />
        <meta name="_csrf" content="${_csrf.token}">
        <meta name="_csrf_header" content="${_csrf.headerName}">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin_v2.css">
    </head>

    <body>
        <jsp:include page="/WEB-INF/views/board/top_common_menu.jsp" />

        <div class="admin-wrapper">

            <!-- 상단 현황판 -->
            <div class="stat-grid">
                <div class="stat-card">
                    <div class="stat-label">전체 회원</div>
                    <div class="stat-value">${stats.total}</div>
                </div>
                <div class="stat-card stat-active">
                    <div class="stat-label">활성</div>
                    <div class="stat-value">${stats.active}</div>
                </div>
                <div class="stat-card stat-banned">
                    <div class="stat-label">정지</div>
                    <div class="stat-value">${stats.banned}</div>
                </div>
                <div class="stat-card stat-deleted">
                    <div class="stat-label">탈퇴</div>
                    <div class="stat-value">${stats.deleted}</div>
                </div>
            </div>

            <!-- 하단: 탭 + 테이블 -->
            <div class="admin-card">

                <!-- 탭 버튼 -->
                <div class="tab-bar">
                    <a href="?tab=all"     class="tab-btn tab-all     ${tab eq 'all'     ? 'active' : ''}">전체 회원</a>
                    <a href="?tab=banned"  class="tab-btn tab-banned  ${tab eq 'banned'  ? 'active' : ''}">정지 회원</a>
                    <a href="?tab=deleted" class="tab-btn tab-deleted ${tab eq 'deleted' ? 'active' : ''}">탈퇴 회원</a>
                </div>

                <div class="admin-card-body">
                    <table class="admin-table admin-table-v2">
                        <thead>
                            <tr>
                                <th>UID</th>
                                <th>닉네임</th>
                                <th>이메일</th>
                                <th>가입일</th>
                                <th>처리일자</th>
                                <th>상태</th>
                                <th>관리</th>
                            </tr>
                        </thead>

                        <tbody>
                            <c:forEach var="m" items="${memberPage.content}">
                                <tr>
                                    <td>${m.memberId}</td>
                                    <td>${m.nickname}</td>
                                    <td>${m.email}</td>
                                    <td>${m.createdAt.toLocalDate()}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${m.status eq 'BANNED'}">${m.updatedAt.toLocalDate()}</c:when>
                                            <c:when test="${m.status eq 'DELETED'}">${m.deletedAt.toLocalDate()}</c:when>
                                            <c:otherwise>-</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><span class="status-badge status-${m.status}">${m.status}</span></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${m.status eq 'ACTIVE'}">
                                                <button class="btn-action btn-ban"      data-id="${m.memberId}" onclick="banMember(this)">정지</button>
                                                <button class="btn-action btn-withdraw" data-id="${m.memberId}" onclick="forceWithdraw(this)">탈퇴</button>
                                            </c:when>
                                            <c:when test="${m.status eq 'BANNED'}">
                                                <button class="btn-action btn-unban" data-id="${m.memberId}" onclick="unbanMember(this)">해제</button>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="deleted-label">탈퇴됨</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <!-- 페이지네이션 -->
                    <c:if test="${pageInfo.totalPages > 0}">
                        <div class="admin-pagination">
                            <c:if test="${pageInfo.currentPage > 0}">
                                <a href="?tab=${tab}&page=${pageInfo.currentPage - 1}">◀</a>
                            </c:if>
                            <c:forEach begin="${pageInfo.startPage}" end="${pageInfo.endPage}" var="i">
                                <a href="?tab=${tab}&page=${i}" class="${pageInfo.currentPage == i ? 'active' : ''}">${i + 1}</a>
                            </c:forEach>
                            <c:if test="${pageInfo.currentPage < pageInfo.totalPages - 1}">
                                <a href="?tab=${tab}&page=${pageInfo.currentPage + 1}">▶</a>
                            </c:if>
                        </div>
                    </c:if>

                </div>
                <c:if test="${empty memberPage.content}">
                    <p class="empty">해당 회원이 없습니다.</p>
                </c:if>
            </div>

        </div>

        <script src="${pageContext.request.contextPath}/js/admin/member.js"></script>

    </body>
</html>
