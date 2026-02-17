<%-- 
    Document   : portfolio_list_body_mk2
    Created on : 2026. 2. 15., 오후 4:07:05
    Author     : Haruki
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h2 class="board-title">Portfolio</h2>

<div class="post-grid">

    <!-- 더미 카드 1 -->
    <a class="post-card" href="#">
        <div class="card-thumb">
            <img src="https://picsum.photos/800/450?random=1">
        </div>

        <div class="card-body">
            <h3 class="card-title">
                Spring Boot 기반 포트폴리오 사이트 제작
            </h3>

            <p class="card-summary">
                JSP + Spring Boot + JPA 기반으로 게시판을 설계하고,
                대규모 트래픽을 고려하여 구조를 리팩토링한 프로젝트입니다.
            </p>

            <div class="card-meta">
                <span class="card-date">2026.02.15</span>

                <div class="meta-bottom">
                    <span class="card-author">by Haruki</span>
                    <span class="card-likes">♥ 24</span>
                </div>
            </div>
        </div>
    </a>

    <!-- 카드 복붙해서 6~8개 정도 테스트용 -->
    <a class="post-card" href="#">
        <div class="card-thumb">
            <img src="https://picsum.photos/800/450?random=2">
        </div>
        <div class="card-body">
            <h3 class="card-title">
                Redis 기반 중복 요청 방지 설계
            </h3>
            <p class="card-summary">
                Idempotency Key 구조를 적용하여 실무 수준의
                중복 저장 방지 시스템을 설계했습니다.
            </p>
            <div class="card-meta">
                <span class="card-date">2026.02.14</span>
                <div class="meta-bottom">
                    <span class="card-author">by Haruki</span>
                    <span class="card-likes">♥ 18</span>
                </div>
            </div>
        </div>
    </a>

</div>
