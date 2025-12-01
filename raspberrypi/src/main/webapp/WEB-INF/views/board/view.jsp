<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<!DOCTYPE html>
<html lang="ko">
    <head>
        <meta charset="UTF-8">
        <title>${post.title}</title>
        <link rel="stylesheet" href="https://uicdn.toast.com/editor/latest/toastui-editor-viewer.min.css">
        <script src="https://uicdn.toast.com/editor/latest/toastui-editor-viewer.min.js"></script>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
        <link rel="stylesheet" type="text/css"
              href="${pageContext.request.contextPath}/css/post.css">
    </head>
    <body>
        <div class="container">

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
            <div id="viewer"></div>

            <!-- 숨겨진 HTML 원본 (Viewer로 전달할 값) : 이스케이프 문자 방지 -->
            <!--viewer-content는 "JSP → HTML → JS" 브릿지 역할-->
            <div id="viewer-content" style="display:none;"><c:out value="${post.content}" escapeXml="false"/></div>

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
                    <a href="${pageContext.request.contextPath}/board/list" class="btn">목록</a>
                </div>
                <div>
                    <!-- 로그인 했을 때만 수정/삭제 버튼 조건 체크 -->
                    <sec:authorize access="isAuthenticated() and ${post.authorId} == authentication.principal.member.id">
                        <form method="get" action="${pageContext.request.contextPath}/board/updateForm/${post.id}" style="display:inline;">
                            <button type="submit" class="btn">✏ 수정</button>
                        </form>

                        <form method="post" action="${pageContext.request.contextPath}/board/delete/${post.id}" style="display:inline;">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                            <button type="submit" class="btn btn-danger" onclick="return confirm('삭제하시겠습니까?');">🗑 삭제</button>
                        </form>
                    </sec:authorize>
                </div>
            </div>
        </div>

        <!-- Viewer 렌더링 -->
        <script>
            const viewer = new toastui.Editor({
                el: document.querySelector('#viewer'),
                viewer: true,
                initialValue: document.getElementById('viewer-content').innerHTML,
                initialValueType: 'html'
            });
        </script>
    </body>
</html>
