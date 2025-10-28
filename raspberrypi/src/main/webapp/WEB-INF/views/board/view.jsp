<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<!DOCTYPE html>
<html lang="ko">
    <head>
        <meta charset="UTF-8">
        <title>${post.title}</title>
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
                    <span>작성자: ${post.author} (${post.maskedIp})</span>
                    <span>작성일: ${post.formattedCreatedAt}</span>
                </div>
            </div>

            <!-- 본문 -->
            <div class="post-content">${post.content}</div>


            <!-- 첨부파일 -->
            <c:if test="${not empty post.attachments}">
                <div class="attachments">
                    <c:forEach var="file" items="${post.attachments}">
                        <c:set var="physicalName" value="${file.uuid}.${file.ext}" />

                        <c:choose>
                            <%-- 이미지 파일 미리보기 --%>
                            <c:when test="${file.ext == 'png' or file.ext == 'jpg' or file.ext == 'jpeg' or file.ext == 'gif'}">
                                <img src="${pageContext.request.contextPath}/upload/${physicalName}"
                                     class="attachments" alt="${file.originalName}" />
                            </c:when>

                            <%-- 그 외 파일은 다운로드 링크 표시 --%>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/download/${file.uuid}">
                                    ${file.originalName}
                                </a>
                            </c:otherwise>
                        </c:choose>

                    </c:forEach>
                </div>
            </c:if>




            <!-- 하단 버튼 -->
            <div class="post-footer">
                <div>
                    <a href="${pageContext.request.contextPath}/board/list" class="btn">목록</a>
                </div>
                <div>
                    <a href="${pageContext.request.contextPath}/board/updateForm/${post.id}" class="btn">수정</a>
                    <form method="post" action="/board/delete/${post.id}" style="display:inline;">
                        <button type="submit" class="btn btn-danger">삭제</button>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
