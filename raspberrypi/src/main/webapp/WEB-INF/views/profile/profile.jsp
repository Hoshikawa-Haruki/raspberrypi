<%-- 
    Document   : profile
    Created on : 2025. 12. 25., 오후 4:27:04
    Author     : Haruki
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="/WEB-INF/views/board/head.jsp" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/profile.css">

        <title>짭케 마이너 갤러리 프로필</title>
    </head>
    <body>
        <jsp:include page="/WEB-INF/views/board/top_common_menu.jsp" />
        <div class="career-page">

            <!-- 좌측 -->
            <aside class="left">
                <h1 class="title">CAREER</h1>

                <section class="block">
                    <h3>프로필</h3>
                    <p class="intro">
                        <!--                        Java / Spring 기반 백엔드 개발자<br>
                                                웹 서비스 설계·구현 중심-->
                        <!--                        백엔드 및 시스템 개발자 <br>
                                                서비스 설계·구현과 운영까지 고려한 개발을 지향합니다-->
                        난 브레이크가 고장난 에잇톤 트럭
                    </p>
                </section>

                <section class="block">
                    <h3>개인 정보 1</h3>
                    <div class="profile-row">
                        <span class="profile-title">이름</span>
                        <span class="profile-content">
                            김갈치
                        </span>
                    </div>

                    <div class="profile-row">
                        <span class="profile-title">이메일</span>
                        <span class="profile-content">
                            hairtai0524@gmail.com
                        </span>
                    </div>

                    <div class="profile-row">
                        <span class="profile-title">연락처</span>
                        <span class="profile-content">
                            010-1234-5678
                        </span>
                    </div>
                </section>

                <section class="block">
                    <h3>개인 정보 2</h3>
                    <ul class="info-list">
                        <li>동의대학교 컴퓨터소프트웨어공학과 전공</li>
                        <li>GPA 4.27 / 4.5</li>
                    </ul>
                </section>

                <section class="block">
                    <h3>링크</h3>
                    <ul class="link-list">
                        <li><a href="#">GitHub</a></li>
                        <li><a href="#">Portfolio Site</a></li>
                    </ul>
                </section>
            </aside>

            <!-- 우측 -->
            <main class="right">

                <section class="section">
                    <h2>전공 & 학력</h2>
                    <div class="row">
                        <span class="period">2020.03 – 2026.02</span>
                        <span>동의대학교 컴퓨터소프트웨어공학과 졸업</span>
                    </div>
                </section>

                <section class="section">
                    <h2>Skills</h2>
                    <div class="skill-row">
                        <span class="skill-title">Backend</span>
                        <span class="skill-content">
                            Java, Spring Boot, Spring MVC (REST API), Spring Security, JPA
                        </span>
                    </div>

                    <div class="skill-row">
                        <span class="skill-title">DB</span>
                        <span class="skill-content">Oracle SQL, MySQL, MariaDB</span>
                    </div>

                    <div class="skill-row">
                        <span class="skill-title">Infra</span>
                        <span class="skill-content">Linux, Nginx, Docker</span>
                    </div>

                    <div class="skill-row">
                        <span class="skill-title">Foundation</span>
                        <span class="skill-content">CS, Data Structure, OS, Network</span>
                    </div>

                </section>

                <section class="section">
                    <h2>Projects</h2>
                    <div class="project">
                        <strong>WebMail System</strong>
                        <span>- Spring 기반 메일 송수신 웹 서비스</span>
                    </div>
                    <div class="project">
                        <strong>Board / Portfolio Service</strong>
                        <span>- 개인 서버(Linux) 환경에서 직접 배포·운영 중인 웹 서비스</span>

                    </div>
                </section>

                <section class="section section-split">
                    <div class="half">
                        <h2>자격증</h2>
                        <ul>
                            <li>정보처리기사</li>
                            <li>SQLD</li>
                            <li>일본어능력시험 2급 (JLPT N2)</li>
                        </ul>
                    </div>

                    <div class="half">
                        <h2>Languages</h2>

                        <div class="split-row">
                            <span class="split-title">Korean</span>
                            <span class="split-content">
                                Native
                            </span>
                        </div>

                        <div class="split-row">
                            <span class="split-title">Japanese</span>
                            <span class="split-content">
                                Fluent
                            </span>
                        </div>

                        <div class="split-row">
                            <span class="split-title">English</span>
                            <span class="split-content">
                                Conversational
                            </span>
                        </div>
                    </div>
                </section>
            </main>
        </div>

        <jsp:include page="/WEB-INF/views/board/footer.jsp"/>

    </body>
</html>
