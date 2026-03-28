<%-- 
    Document   : list
    Created on : 2025. 10. 13., 오후 1:46:49
    Author     : Haruki
--%>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
    <head>
        <title>RetroDays</title>
        <jsp:include page="/WEB-INF/views/board/head.jsp" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
        <link rel="stylesheet" type="text/css"
              href="${pageContext.request.contextPath}/css/list.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pagination.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/search.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">

    </head>

    <body>
        <header>       
            <jsp:include page="/WEB-INF/views/board/top_common_menu.jsp" />
        </header>

        <main>
            <div class="container">
                <jsp:include page="/WEB-INF/views/board/list_body.jsp" />
                <jsp:include page="/WEB-INF/views/board/pagination.jsp" />
                <jsp:include page="/WEB-INF/views/board/search.jsp" />
            </div>
        </main>

        <footer class="site-footer">
            <jsp:include page="/WEB-INF/views/board/footer.jsp"/>
        </footer>
    </body>

</html>
