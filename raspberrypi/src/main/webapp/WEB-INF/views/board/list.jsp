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
        <title>짭케 마이너 갤러리</title>
        <jsp:include page="/WEB-INF/views/board/head.jsp" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
        <link rel="stylesheet" type="text/css"
              href="${pageContext.request.contextPath}/css/list.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/search.css">
    </head>

    <body>
        <jsp:include page="/WEB-INF/views/board/top_common_menu.jsp" />

        <div class="container">
            <jsp:include page="/WEB-INF/views/board/list_body.jsp" />
            <jsp:include page="/WEB-INF/views/board/pagination.jsp" />
            <jsp:include page="/WEB-INF/views/board/search.jsp" />
        </div>

        <jsp:include page="/WEB-INF/views/board/footer.jsp"/>
    </body>

</html>
