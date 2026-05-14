<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <title>관리자</title>
        <jsp:include page="/WEB-INF/views/board/head.jsp" />
        <meta name="_csrf" content="${_csrf.token}">
        <meta name="_csrf_header" content="${_csrf.headerName}">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/list.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
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

            <!-- 하단 좌우 분할 -->
            <div class="admin-content">

                <!-- 좌측: 전체 회원 목록 -->
                <div class="admin-card">
                    <div class="admin-card-header">전체 회원 목록</div>
                    <div class="admin-card-body">

                        <table class="admin-table">
                            <thead>
                                <tr>
                                    <th>번호</th>
                                    <th>닉네임</th>
                                    <th>이메일</th>
                                    <th>가입일</th>
                                    <th>상태</th>
                                    <th>관리</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="m" items="${memberPage.content}">
                                    <tr id="row-${m.memberId}">
                                        <td>${m.memberId}</td>
                                        <td>${m.nickname}</td>
                                        <td>${m.email}</td>
                                        <td>${m.createdAt.toLocalDate()}</td>
                                        <td><span class="status-badge status-${m.status}" id="badge-${m.memberId}">${m.status}</span></td>
                                        <td class="action-cell" id="action-${m.memberId}">
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
                                    <a href="?page=${pageInfo.currentPage - 1}">◀</a>
                                </c:if>
                                <c:forEach begin="${pageInfo.startPage}" end="${pageInfo.endPage}" var="i">
                                    <a href="?page=${i}" class="${pageInfo.currentPage == i ? 'active' : ''}">${i + 1}</a>
                                </c:forEach>
                                <c:if test="${pageInfo.currentPage < pageInfo.totalPages - 1}">
                                    <a href="?page=${pageInfo.currentPage + 1}">▶</a>
                                </c:if>
                            </div>
                        </c:if>

                    </div>
                </div>

                <!-- 우측: 정지/탈퇴 회원 -->
                <div class="admin-card">
                    <div class="admin-card-header danger">정지 / 탈퇴 회원</div>
                    <div class="admin-card-body">

                        <c:if test="${empty inactiveMemberPage.content}">
                            <p class="empty">해당 회원이 없습니다.</p>
                        </c:if>

                        <table class="admin-table inactive-table">
                            <colgroup>
                                <col style="width: auto">
                                <col style="width: 60px">
                                <col style="width: 90px">
                                <col style="width: 60px">
                            </colgroup>
                            <thead>
                                <tr>
                                    <th>닉네임</th>
                                    <th>상태</th>
                                    <th>처리일</th>
                                    <th>관리</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="m" items="${inactiveMemberPage.content}">
                                    <tr>
                                        <td>${m.nickname}</td>
                                        <td><span class="status-badge status-${m.status}">${m.status}</span></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${m.status eq 'DELETED'}">${m.deletedAt.toLocalDate()}</c:when>
                                                <c:otherwise>${m.updatedAt.toLocalDate()}</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:if test="${m.status eq 'BANNED'}">
                                                <button class="btn-action btn-unban" data-id="${m.memberId}" onclick="unbanMember(this)">해제</button>
                                            </c:if>
                                            <c:if test="${m.status eq 'DELETED'}">
                                                <span class="deleted-label">-</span>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>

                        <!-- 페이지네이션 -->
                        <c:if test="${inactivePageInfo.totalPages > 0}">
                            <div class="admin-pagination">
                                <c:if test="${inactivePageInfo.currentPage > 0}">
                                    <a href="?inactivePage=${inactivePageInfo.currentPage - 1}">◀</a>
                                </c:if>
                                <c:forEach begin="${inactivePageInfo.startPage}" end="${inactivePageInfo.endPage}" var="i">
                                    <a href="?inactivePage=${i}" class="${inactivePageInfo.currentPage == i ? 'active' : ''}">${i + 1}</a>
                                </c:forEach>
                                <c:if test="${inactivePageInfo.currentPage < inactivePageInfo.totalPages - 1}">
                                    <a href="?inactivePage=${inactivePageInfo.currentPage + 1}">▶</a>
                                </c:if>
                            </div>
                        </c:if>

                    </div>
                </div>

            </div>
        </div>

        <script src="${pageContext.request.contextPath}/js/admin/member.js"></script>

    </body>
</html>
