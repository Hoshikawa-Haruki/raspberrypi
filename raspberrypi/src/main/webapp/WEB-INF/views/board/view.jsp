<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<!DOCTYPE html>
<html lang="ko">
    <head>
        <meta charset="UTF-8">
        <title>${post.title}</title>
        <jsp:include page="/WEB-INF/views/board/head.jsp" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/list.css">
        <link rel="stylesheet" type="text/css"
              href="${pageContext.request.contextPath}/css/post.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/search.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/comment.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">

    </head>

    <body>
        <jsp:include page="/WEB-INF/views/board/top_common_menu.jsp" />

        <div class="container" id="post-container">
            <!-- 게시글 상단 -->
            <div class="post-header">
                <div class="post-title"> <c:out value="${post.title}"/>
                </div>
                <div class="post-info">
                    <span class="left-info">${post.authorNameSnapshot} (${post.maskedIp})</span>
                    |
                    <span class="right-info">${post.formattedCreatedAt}</span>
                </div>
            </div>
            <!-- 본문 -->
            <div class="post-content">${post.content}</div>

            <!-- 첨부파일 -->
            <c:if test="${not empty post.attachments}">
                <fieldset class="attachments-box">
                    <legend>첨부파일</legend>
                    <c:forEach var="file" items="${post.attachments}" varStatus="s">
                        <span class="file-item">
                            <a href="${pageContext.request.contextPath}/download/${file.uuid}">${file.originalName}</a>
                        </span><!--
                        --><c:if test="${!s.last}">
                            <span class="separator"> | </span>
                        </c:if>
                    </c:forEach>
                </fieldset>
            </c:if>

            <!-- 하단 버튼 -->
            <div class="post-footer">
                <div>
                    <a href="${pageContext.request.contextPath}/board/list" class="btn btn-list">목록</a>
                </div>
                <div>
                    <!-- 로그인 했을 때만 수정/삭제 버튼 조건 체크 -->
                    <c:if test="${loginMemberId != null && post.authorId == loginMemberId}">
                        <form method="get" action="${pageContext.request.contextPath}/board/updateForm/${post.id}" style="display:inline;">
                            <button type="submit" class="btn">✏ 수정</button>
                        </form>

                        <form method="post" action="${pageContext.request.contextPath}/board/delete/${post.id}" style="display:inline;">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

                            <!-- 검색 상태 전달 -->
                            <input type="hidden" name="searchType" value="${searchType}">
                            <input type="hidden" name="keyword" value="${keyword}">
                            <input type="hidden" name="page" value="${param.page}">

                            <button type="submit" class="btn btn-danger" onclick="return confirm('삭제하시겠습니까?');">🗑 삭제</button>
                        </form>
                    </c:if>
                </div>
            </div>
        </div>
        <div class="comment-container">
            <jsp:include page="/WEB-INF/views/board/comment.jsp" />
        </div>
        <div class="container">
            <jsp:include page="/WEB-INF/views/board/list_body.jsp" />
            <jsp:include page="/WEB-INF/views/board/pagination.jsp" />
            <jsp:include page="/WEB-INF/views/board/search.jsp" />
        </div>

        <footer class="site-footer">
            <jsp:include page="/WEB-INF/views/board/footer.jsp"/>
        </footer>

        <script type="module"
        src="${pageContext.request.contextPath}/js/pages/board-view.js"></script>

    </body>
</html>
