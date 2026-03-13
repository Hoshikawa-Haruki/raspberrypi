<%-- 
    Document   : portfolio_list_body
    Created on : 2026. 2. 15., 오후 3:50:25
    Author     : Haruki
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="board-header">
    <h2 class="board-title">📂 Portfolio</h2>

    <div class="write-actions">
        <a class="btn btn-write"
           href="${pageContext.request.contextPath}/portfolio/writeForm">
            ✏️ 새 포폴 작성
        </a>
    </div>
</div>

<div class="post-grid">

    <c:forEach var="p" items="${portfolioList}">

        <a class="post-card"
           href="${pageContext.request.contextPath}/portfolio/${p.id}">

            <div class="card-thumb">
                <img src="${p.thumbnailUrl}">
            </div>

            <div class="card-body">
                <div class="card-main">
                    <h3 class="card-title">
                        ${p.title}
                    </h3>

                    <p class="card-summary">
                        ${p.summary}
                    </p>
                </div>

                <!-- 태그 영역 -->
                <div class="portfolio-list-tag-area">

                    <!-- 프로젝트 기간 태그 -->
                    <c:if test="${not empty p.formattedProjectPeriod}">
                        <div class="tag-pill">
                            <span class="tag period">
                                ${p.formattedProjectPeriod}
                            </span>
                        </div>
                    </c:if>

                    <!-- 기술 스택 태그들 -->
                    <c:if test="${not empty p.techStack}">
                        <div class="tag-pill">
                            <c:forEach var="tag"
                                       items="${fn:split(p.techStack, ',')}">
                                <span class="tag tech">
                                    ${fn:trim(tag)}
                                </span>
                            </c:forEach>
                        </div>
                    </c:if>
                </div>

                <div class="card-meta">
                    <div class="card-meta-top">
                        <div class="author-area">
                            <img class="author-img"
                                 src="https://picsum.photos/40?random=10">
                            <span class="author-name">
                                ${p.authorNameSnapshot}
                            </span>
                        </div>
                    </div>
                    <div class="card-meta-bottom">
                        ${p.formattedCreatedAt} · 댓글 ${p.commentCount}개
                    </div>
                </div>

            </div>
        </a>
    </c:forEach>

    <a class="post-card" href="#">
        <div class="card-thumb">
            <img src="https://picsum.photos/500/400?random=2">
        </div>

        <div class="card-body">
            <h3 class="card-title">
                Redis기반 중복요청 방지 시스템 구현
            </h3>
            <p class="card-summary">
                Idempotency Key 설계를 통해 중복 저장을 방지하고,
                실제 트래픽 환경을 고려한 구조를 설계했습니다.
            </p> <div class="card-meta">
                <div class="card-meta-top">
                    2026-02-15 · 댓글 3개
                </div>

                <div class="card-meta-bottom">
                    <div class="author-area">
                        <img class="author-img" src="https://picsum.photos/40?random=10">
                        <span class="author-name">Haruki</span>
                    </div>
                    <div class="likes">
                        ♥ 127
                    </div>

                </div>
            </div>
        </div>
    </a>

</div>



