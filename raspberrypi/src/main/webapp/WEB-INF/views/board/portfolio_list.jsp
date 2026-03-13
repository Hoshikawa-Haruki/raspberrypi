<%-- 
    Document   : portfolio_list
    Created on : 2026. 2. 15., 오후 3:53:30
    Author     : Haruki
--%>

<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>
    <head>
        <title>짭케 마이너 갤러리</title>
        <jsp:include page="/WEB-INF/views/board/head.jsp" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
        <link rel="stylesheet" type="text/css"
              href="${pageContext.request.contextPath}/css/list.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/portfolio_list.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/search.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">

    </head>

    <body>
        <header>
            <jsp:include page="/WEB-INF/views/board/top_common_menu.jsp" />
        </header>

        <main>
            <div class="container">
                <jsp:include page="/WEB-INF/views/board/portfolio_list_body.jsp" />
                <jsp:include page="/WEB-INF/views/board/pagination.jsp" />
                <jsp:include page="/WEB-INF/views/board/search.jsp" />
            </div>
        </main>

        <footer class="site-footer">
            <jsp:include page="/WEB-INF/views/board/footer.jsp"/>
        </footer>
    </body>

</html>
