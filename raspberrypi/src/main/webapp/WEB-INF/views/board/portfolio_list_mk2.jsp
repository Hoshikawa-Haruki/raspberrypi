<%-- 
    Document   : portfolio_list_mk2
    Created on : 2026. 2. 17., 오후 6:54:38
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
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/portfolio_list_mk2.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/search.css">
    </head>

    <body>
        <jsp:include page="/WEB-INF/views/board/top_common_menu.jsp" />

        <div class="container">
            <jsp:include page="/WEB-INF/views/board/portfolio_list_body_mk2.jsp" />
            <jsp:include page="/WEB-INF/views/board/pagination.jsp" />
            <jsp:include page="/WEB-INF/views/board/search.jsp" />
        </div>

        <jsp:include page="/WEB-INF/views/board/footer.jsp"/>
    </body>

</html>
