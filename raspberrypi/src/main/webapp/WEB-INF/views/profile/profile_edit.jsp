<%-- 
    Document   : profile_edit
    Created on : 2026. 1. 5., 오후 5:01:31
    Author     : Haruki

2026.01.05. 동적 profile 구현 중지
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/profile.css">
        <title>프로필 수정</title>
    </head>

    <body>
        <jsp:include page="/WEB-INF/views/board/top_common_menu.jsp" />

        <form method="post" action="${pageContext.request.contextPath}/profile/edit">
            <div class="career-page">

                <!-- 좌측 -->
                <aside class="left">
                    <h1 class="title">CAREER</h1>

                    <section class="block">
                        <h3>프로필</h3>
                        <textarea name="intro" class="intro" rows="4">
                            ${profile.intro}
                        </textarea>
                    </section>

                    <section class="block">
                        <h3>개인 정보 1</h3>

                        <div class="carrer-row">
                            <strong>이름</strong>
                            <input type="text" name="name" value="${profile.name}">
                        </div>

                        <div class="carrer-row">
                            <strong>이메일</strong>
                            <input type="text" name="email" value="${profile.email}">
                        </div>

                        <div class="carrer-row">
                            <strong>전화번호</strong>
                            <input type="text" name="phone" value="${profile.phone}">
                        </div>
                    </section>

                    <section class="block">
                        <h3>개인 정보 2</h3>
                        <ul class="info-list">
                            <li>
                                전공:
                                <input type="text" name="major" value="${profile.major}">
                            </li>
                            <li>
                                GPA:
                                <input type="text" name="gpa" value="${profile.gpa}">
                            </li>
                        </ul>
                    </section>
                </aside>

                <!-- 우측 -->
                <main class="right">
                    <section class="section">
                        <h2>전공 & 학력</h2>
                        <textarea name="skills" rows="4">
                            ${profile.career}
                        </textarea>
                    </section>

                    <section class="section">
                        <h2>Skills</h2>
                        <textarea name="skills" rows="4">
                            ${profile.skills}
                        </textarea>
                    </section>

                    <section class="section">
                        <h2>Projects</h2>
                        <textarea name="projects" rows="4">
                            ${profile.projects}
                        </textarea>
                    </section>

                    <section class="section section-split">
                        <div class="half">
                            <h2>자격증</h2>
                            <textarea name="certificates" rows="4">
                                ${profile.certificates}
                            </textarea>
                        </div>

                        <div class="half">
                            <h2>Languages</h2>
                            <textarea name="languages" rows="4">
                                ${profile.languages}
                            </textarea>
                        </div>
                    </section>

                    <!-- 버튼 -->
                    <div style="margin-top: 32px; text-align: right;">
                        <button type="submit" class="btn">저장</button>
                        <a href="${pageContext.request.contextPath}/profile" class="btn btn-cancel">
                            취소
                        </a>
                    </div>

                </main>
            </div>
        </form>

    </body>
</html>

