<%-- 
    Document   : portfolio_list_body
    Created on : 2026. 2. 15., 오후 3:50:25
    Author     : Haruki
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<h2 class="board-title">📂 Portfolio</h2>

<div class="post-grid">

    <!-- 더미 카드 1 -->
    <a class="post-card" href="#">
        <div class="card-thumb">
            <img src="https://picsum.photos/500/400?random=1">
        </div>

        <div class="card-body">
            <h3 class="card-title">
                Spring Boot 기반 포트폴리오 사이트 제작
            </h3>

            <p class="card-summary">
                JSP + Spring Boot + JPA 구조로 게시판을 설계하고,
                대규모 트래픽 대응을 고려하여 리팩토링을 진행한 프로젝트입니다.
            </p>

            <div class="card-meta">
                <div class="card-meta-top">
                    2026-02-15 · 댓글 3개
                </div>

                <div class="card-meta-bottom">
                    <div class="author-area">
                        <img class="author-img"
                             src="https://picsum.photos/40?random=10">
                        <span class="author-name">Haruki</span>
                    </div>
                    <div class="likes">
                        ♥ 127
                    </div>
                </div>
            </div>
            
        </div>
    </a>

    <a class="post-card" href="#">
        <div class="card-thumb">
            <img src="https://picsum.photos/500/400?random=2">
        </div>
        <div class="card-body">
            <h3 class="card-title">
                Redis 기반 중복 요청 방지 시스템 구현
            </h3>
            <p class="card-summary">
                Idempotency Key 설계를 통해 중복 저장을 방지하고,
                실제 트래픽 환경을 고려한 구조를 설계했습니다.
            </p>

            <div class="card-meta">
                <div class="card-meta-top">
                    2026-02-15 · 댓글 3개
                </div>

                <div class="card-meta-bottom">
                    <div class="author-area">
                        <img class="author-img"
                             src="https://picsum.photos/40?random=10">
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


