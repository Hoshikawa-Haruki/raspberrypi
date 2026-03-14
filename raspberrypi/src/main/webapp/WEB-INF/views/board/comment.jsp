<%-- 
    Document   : comment
    Created on : 2026. 2. 2., 오후 3:02:39
    Author     : Haruki
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!-- 댓글 영역 -->
<div class="comment-section">

    <!-- 댓글 헤더 -->
    <div class="comment-header">
        <span class="comment-count">
            전체 댓글 [<span id="comment-count">0</span>]
        </span>

        <div class="comment-actions">
            <a href="#" id="go-title">본문 보기</a>
            <a href="#" id="comment-refresh">새로고침</a>
        </div>
    </div>

    <!-- 댓글 목록 -->
    <div class="comment-list" id="comment-list"></div>
    <div class="comment-pagination" id="comment-pagination"></div>

    <!-- 댓글 작성 -->
    <div id="comment-form" class="comment-form">

        <div class="comment-write-box">
            <div class="comment-writer">
                <span class="writer-name">
                    <sec:authorize access="isAuthenticated()">
                        ${loginUserNickname}
                    </sec:authorize>
                    <sec:authorize access="isAnonymous()">
                        로그인이 필요합니다
                    </sec:authorize>
                </span>
            </div>


            <div class="comment-input-area">
                <textarea id="comment-content"
                          maxlength="1000"
                          placeholder="타인의 권리를 침해하거나 명예를 훼손하는 댓글은 운영원칙 및 관련 법률에 의해 제재를 받을 수 있습니다."></textarea>
            </div>
        </div>

        <div class="comment-action-bar">
            <div class="right-actions">
                <button type="button"
                        id="comment-submit"
                        class="btn-submit">
                    등록
                </button>
            </div>
        </div>
    </div>

</div>

<script>
    window.POST_ID = ${post.id};
    window.CSRF_TOKEN = '${_csrf.token}';
</script>
<script src="${pageContext.request.contextPath}/js/board/comments.js"></script>

