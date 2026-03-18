<%-- 
    Document   : withdraw-success
    Created on : 2026. 3. 17., 오후 5:17:52
    Author     : Haruki
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>회원탈퇴 완료</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/mypage.css">

        <style>
            .withdraw-complete {
                text-align: center;
                padding: 60px 20px;
            }

            .withdraw-complete h2 {
                font-size: 22px;
                margin-bottom: 12px;
                color: #dc3545;
            }

            .withdraw-complete p {
                color: #666;
                margin-bottom: 25px;
            }

            .withdraw-actions {
                display: flex;
                justify-content: center;
                gap: 10px;
            }

            .btn-home {
                padding: 10px 18px;
                border-radius: 6px;
                background: #2c7be5;
                color: white;
                text-decoration: none;
                font-size: 14px;
            }

            .btn-home:hover {
                background: #1a68d1;
            }
        </style>
    </head>

    <body>

        <jsp:include page="/WEB-INF/views/board/top_common_menu.jsp" />

        <div class="mypage-wrapper">
            <div class="mypage-container">

                <!-- 좌측 (비워도 되고 유지해도 됨) -->
                <aside class="mypage-left">
                    <div class="profile-box">
                        <div class="avatar"></div>
                        <div class="username">탈퇴한 사용자 입니다</div>
                        <div class="email">Goodbye 👋</div>
                    </div>

                    <ul class="mypage-menu">
                        <li class="danger active">회원탈퇴 완료</li>
                    </ul>
                </aside>

                <!-- 우측 -->
                <section class="mypage-right">

                    <div class="card">
                        <div class="card-header danger-header">회원 탈퇴 완료</div>
                        <div class="card-body withdraw-complete">

                            <h2>회원 탈퇴가 완료되었습니다.</h2>
                            <p>그동안 서비스를 이용해주셔서 감사합니다.</p>

                            <div class="withdraw-actions">
                                <a href="${pageContext.request.contextPath}/" class="btn-home">
                                    메인으로 이동
                                </a>
                                <a href="${pageContext.request.contextPath}/member/loginForm" class="btn-cancel">
                                    다시 로그인
                                </a>
                            </div>

                        </div>
                    </div>

                </section>

            </div>
        </div>

    </body>
</html>
