<%-- 
    Document   : comment
    Created on : 2026. 2. 2., 오후 3:02:39
    Author     : Haruki
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- 댓글 영역 -->
<div class="comment-section">

    <!-- 댓글 헤더 -->
    <div class="comment-header">
        <span class="comment-count">전체 댓글 [${comments.size()}]</span>
        <div class="comment-actions">
            <a href="#post">본문 보기</a>
            <a href="">새로고침</a>
        </div>
    </div>

    <!-- 댓글 목록 -->
    <div class="comment-list">
        <c:forEach var="comment" items="${comments}">
            <div class="comment-item">
                <div class="comment-meta">
                    <span class="comment-author">${comment.authorNameSnapshot}</span>
                    <span class="comment-time">${comment.formattedCreatedAt}</span>

                    <c:if test="${comment.mine}">
                        <button class="comment-delete">×</button>
                    </c:if>
                </div>

                <div class="comment-content">
                    ${comment.content}
                </div>
            </div>
        </c:forEach>
    </div>

    <!-- 댓글 작성 (디시 스타일) -->
    <form id="comment-form"
          class="comment-form"
          method="post"
          action="${pageContext.request.contextPath}/comment/create">

        <input type="hidden" name="postId" value="${post.id}" />
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>


        <div class="comment-write-box">
            <!-- 좌측 닉네임 -->
            <div class="comment-writer">
                <span class="writer-name">
                    ${loginUserNickname}
                </span>
            </div>

            <!-- 우측 입력 -->
            <div class="comment-input-area">
                <textarea name="content"
                          maxlength="1000"
                          placeholder="타인의 권리를 침해하거나 명예를 훼손하는 댓글은 운영원칙 및 관련 법률에 의해 제재를 받을 수 있습니다."
                          required></textarea>
            </div>
        </div>

        <!-- 하단 액션 바 -->
        <div class="comment-action-bar">
            <div class="right-actions">
                <button type="submit" class="btn-submit">등록</button>
            </div>
        </div>
    </form>

</div>
