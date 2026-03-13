<%-- 
    Document   : update_toastui_ver2
    Created on : 2025. 11. 24., 오전 1:23:37
    Author     : Haruki
--%>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <title>짭케 마이너 갤러리</title>
        <jsp:include page="/WEB-INF/views/board/head.jsp" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/write.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/portfolio_write.css">
        <link rel="stylesheet" href="https://uicdn.toast.com/editor/latest/toastui-editor.min.css">
        <script src="https://uicdn.toast.com/editor/latest/toastui-editor-all.min.js"></script>
    </head>
    <body>
        <jsp:include page="/WEB-INF/views/board/top_common_menu.jsp" />
        <div class="container">
            <h2>✏️ 위지윅 수정</h2>

            <form id="postForm" method="post" enctype="multipart/form-data"
                  action="${pageContext.request.contextPath}/portfolio/update/${post.id}">

                <input type="text" name="title" class="title-input" maxlength="40" value="${post.title}"
                       placeholder="제목을 입력해 주세요." required>
                <div class="portfolio-meta">
                    <div class="meta-item">
                        <label>프로젝트 기간</label>
                        <div class="project-range">
                            <input type="date"
                                   name="projectStart"
                                   value="${post.projectStart}">

                            <span>~</span>

                            <input type="date"
                                   name="projectEnd"
                                   value="${post.projectEnd}">

                        </div>
                    </div>

                    <div class="meta-item">
                        <label>기술 스택</label>
                        <div class="tag-box" id="tagBox">
                            <input type="text" id="tagInput" placeholder="기술 입력 후 Enter">
                        </div>
                        <!-- 실제 서버 전송용 -->
                        <input type="hidden" name="techStack" id="techStackHidden">
                    </div>
                </div>

                <!-- 에디터 -->
                <div id="editor"></div>
                <!-- 기존 본문 HTML -->
                <textarea id="originContent" style="display:none;">${post.content}</textarea>
                <!-- 최종 전송용 -->
                <textarea id="content" name="content" style="display:none;"></textarea>

                <!-- 기존 첨부파일 목록 -->
                <c:if test="${not empty post.attachments}">

                    <!-- FILE 첨부파일 개수 계산 -->
                    <c:set var="fileCount" value="0" />
                    <c:forEach var="file" items="${post.attachments}">
                        <c:if test="${file.type.name() eq 'FILE'}">
                            <c:set var="fileCount" value="${fileCount + 1}" />
                        </c:if>
                    </c:forEach>

                    <!-- FILE 첨부파일이 있을 때 -->
                    <c:if test="${fileCount > 0}">
                        <p><strong>기존 첨부파일</strong></p>
                        <c:forEach var="file" items="${post.attachments}">
                            <c:if test="${file.type.name() eq 'FILE'}">
                                <label>
                                    <input type="checkbox" name="deleteFileIds" value="${file.id}"> (삭제)
                                    ${file.originalName}
                                </label>
                            </c:if>
                        </c:forEach>

                        <hr/>
                    </c:if>

                    <!-- FILE 첨부파일이 없을 때 -->
                    <c:if test="${fileCount == 0}">
                        <p style="color: #d32f2f;">첨부파일 없음</p>
                    </c:if>

                </c:if>

                <!-- 새 첨부 -->
                <input type="file" name="newFiles" multiple>
                <!-- csrf 토큰 전송 -->
                <input type="hidden" name="_csrf" value="${_csrf.token}">

                <div class="form-actions">
                    <button type="button" id="openModalBtn" class="btn-submit">등록</button>
                    <button type="button" class="btn-cancel"
                            onclick="location.href = '${pageContext.request.contextPath}/board/list'">
                        취소
                    </button>
                </div>

                <!-- idem 키 전송 -->
                <input type="hidden" name="idempotencyKey" id="idempotencyKey">
                <!-- 포트폴리오 추가 정보 모달 -->
                <div id="portfolioModal" class="modal-overlay">
                    <div class="modal-content" id="modalContent">

                        <!--<h3 class="modal-title">포트폴리오 정보 설정</h3>-->

                        <div class="modal-group">
                            <div class="summary-header">
                                <label for="summaryInput">포트폴리오 요약</label>
                                <span id="summaryCounter">0 / 200</span>
                            </div>

                            <textarea id="summaryInput"
                                      name="summary"
                                      maxlength="200"
                                      placeholder="목록에서 표시될 요약입니다.">${post.summary}</textarea>

                            <div class="thumbnail-upload-wrapper">
                                <c:if test="${not empty post.thumbnailUrl}">
                                    <input type="hidden" id="existingThumbnailUrl" value="${post.thumbnailUrl}">
                                </c:if>
                                <input type="file"
                                       id="thumbnailFile"
                                       name="thumbnailFile"
                                       accept="image/*"
                                       hidden>

                                <!-- 업로드 버튼(등록 전) -->
                                <label for="thumbnailFile" class="thumbnail-button" id="thumbnailUploadButton">
                                    썸네일 업로드
                                </label>

                                <!-- 미리보기 영역(등록 후) -->
                                <div class="thumbnail-preview-box" id="previewBox" style="display:none;">
                                    <img id="thumbnailPreview">

                                    <div class="thumbnail-actions">
                                        <span id="reuploadBtn">재업로드</span>
                                        <span>·</span>
                                        <span id="removeBtn">제거</span>
                                    </div>
                                </div>
                            </div>


                        </div>

                        <div class="modal-actions">
                            <button type="button" id="finalSubmitBtn" class="btn-submit">최종 등록</button>
                            <button type="button" id="closeModalBtn" class="btn-cancel">취소</button>
                        </div>

                    </div>
                </div>

            </form>
        </div>

        <!-- JS로 경로 전달 -->
        <div id="editor-config"
             data-upload-url="${pageContext.request.contextPath}/upload/temp">
        </div>

        <!-- update 전용 JS -->
        <script src="${pageContext.request.contextPath}/js/board/toastui-editor.js"></script>
        <script>
                                document.addEventListener("DOMContentLoaded", function () {

                                    const form = document.getElementById("postForm");
                                    const openModalBtn = document.getElementById("openModalBtn");
                                    const modal = document.getElementById("portfolioModal");
                                    const modalContent = document.getElementById("modalContent");
                                    const finalSubmitBtn = document.getElementById("finalSubmitBtn");
                                    const closeModalBtn = document.getElementById("closeModalBtn");

                                    const summaryInput = document.getElementById("summaryInput");
                                    const summaryCounter = document.getElementById("summaryCounter");

                                    const thumbnailInput = document.getElementById("thumbnailFile");
                                    const thumbnailUploadButton = document.getElementById("thumbnailUploadButton");
                                    const previewBox = document.getElementById("previewBox");
                                    const thumbnailPreview = document.getElementById("thumbnailPreview");
                                    const removeBtn = document.getElementById("removeBtn");
                                    const reuploadBtn = document.getElementById("reuploadBtn");


                                    /* ======================
                                     summary 글자 수 카운트
                                     ====================== */
                                    function updateSummaryCounter() {
                                        const length = summaryInput.value.length;
                                        summaryCounter.textContent = length + " / 200";

                                        if (length > 170)
                                            summaryCounter.style.color = "#e03131";
                                        else if (length > 140)
                                            summaryCounter.style.color = "#f08c00";
                                        else
                                            summaryCounter.style.color = "#888";
                                    }

                                    summaryInput.addEventListener("input", updateSummaryCounter);
                                    updateSummaryCounter();


                                    /* ======================
                                     모달 열기
                                     ====================== */
                                    openModalBtn.addEventListener("click", function () {

                                        const title = form.querySelector("input[name='title']").value.trim();
                                        const rawHtml = window.editor.getHTML();
                                        const cleaned = rawHtml
                                                .replace(/<p><br><\/p>/g, "")
                                                .replace(/<[^>]*>/g, "")
                                                .trim();

                                        if (!title || cleaned === "") {
                                            alert("제목과 내용을 먼저 입력하세요.");
                                            return;
                                        }

                                        modal.style.display = "flex";
                                    });


                                    /* ======================
                                     모달 닫기
                                     ====================== */
                                    function closeModal() {
                                        modal.style.display = "none";
                                    }

                                    closeModalBtn.addEventListener("click", closeModal);

                                    document.addEventListener("keydown", function (e) {
                                        if (e.key === "Escape" && modal.style.display === "flex") {
                                            closeModal();
                                        }
                                    });

                                    modal.addEventListener("click", function (e) {
                                        if (!modalContent.contains(e.target)) {
                                            closeModal();
                                        }
                                    });


                                    /* ======================
                                     썸네일 업로드 처리
                                     ====================== */
                                    thumbnailInput.addEventListener("change", function () {

                                        const file = this.files[0];
                                        if (!file)
                                            return;

                                        if (file.size > 5 * 1024 * 1024) {
                                            alert("5MB 이하 이미지만 업로드 가능합니다.");
                                            thumbnailInput.value = "";
                                            previewBox.style.display = "none";
                                            thumbnailUploadButton.style.display = "inline-block";
                                            return;
                                        }

                                        const reader = new FileReader();
                                        reader.onload = function (e) {
                                            thumbnailPreview.src = e.target.result;
                                            thumbnailUploadButton.style.display = "none";
                                            previewBox.style.display = "block";
                                        };

                                        reader.readAsDataURL(file);
                                    });

                                    removeBtn.addEventListener("click", function () {
                                        thumbnailInput.value = "";
                                        thumbnailPreview.src = "";
                                        previewBox.style.display = "none";
                                        thumbnailUploadButton.style.display = "inline-block";
                                    });

                                    reuploadBtn.addEventListener("click", function () {
                                        thumbnailInput.click();
                                    });


                                    /* ======================
                                     최종 등록
                                     ====================== */
                                    finalSubmitBtn.addEventListener("click", function () {

                                        if (summaryInput.value.trim() === "") {
                                            alert("요약을 입력해주세요.");
                                            return;
                                        }

                                        const rawHtml = window.editor.getHTML();
                                        document.getElementById("content").value = rawHtml;

                                        form.submit();
                                    });

                                });
        </script>

        <!-- 기존 태그 화면표시용으로 생성-->
        <script>
            const initialTags = [
            <c:forEach var="tag" items="${post.techStacks}" varStatus="status">
            "${tag}"<c:if test="${!status.last}">,</c:if>
            </c:forEach>
            ] || [];
        </script>

        <script>
            document.addEventListener("DOMContentLoaded", function () {

                /* =========================
                 DOM 요소
                 ========================= */

                const tagInput = document.getElementById("tagInput");
                const tagBox = document.getElementById("tagBox");
                const hiddenInput = document.getElementById("techStackHidden");

                const startInput = document.querySelector("input[name='projectStart']");
                const endInput = document.querySelector("input[name='projectEnd']");

                const thumbnailPreview = document.getElementById("thumbnailPreview");
                const previewBox = document.getElementById("previewBox");
                const thumbnailUploadButton = document.getElementById("thumbnailUploadButton");

                const existingThumb = document.getElementById("existingThumbnailUrl");


                /* =========================
                 프로젝트 기간 제한
                 ========================= */

                startInput.addEventListener("change", () => {
                    endInput.min = startInput.value;
                });

                endInput.addEventListener("change", () => {
                    startInput.max = endInput.value;
                });


                /* =========================
                 기존 썸네일 표시
                 ========================= */

                if (existingThumb && existingThumb.value) {
                    thumbnailPreview.src = existingThumb.value;
                    previewBox.style.display = "block";
                    thumbnailUploadButton.style.display = "none";
                }


                /* =========================
                 태그 관리
                 ========================= */

                let tags = [...initialTags];

                function updateHiddenInput() {
                    hiddenInput.value = tags.join(",");
                }

                function createTagElement(tagText) {

                    const tag = document.createElement("div");
                    tag.classList.add("tag");

                    const text = document.createElement("span");
                    text.textContent = tagText;

                    const button = document.createElement("button");
                    button.type = "button";
                    button.textContent = "×";

                    button.addEventListener("click", function () {
                        tags = tags.filter(t => t !== tagText);
                        tag.remove();
                        updateHiddenInput();
                    });

                    tag.appendChild(text);
                    tag.appendChild(button);

                    return tag;
                }


                /* =========================
                 기존 태그 렌더링
                 ========================= */

                initialTags.forEach(tagText => {
                    const tagElement = createTagElement(tagText);
                    tagBox.insertBefore(tagElement, tagInput);
                });

                updateHiddenInput();


                /* =========================
                 태그 입력 이벤트
                 ========================= */

                tagInput.addEventListener("keydown", function (e) {

                    if (e.key !== "Enter")
                        return;

                    e.preventDefault();

                    const value = tagInput.value.trim();

                    if (!value || tags.includes(value))
                        return;

                    tags.push(value);

                    const tagElement = createTagElement(value);

                    tagBox.insertBefore(tagElement, tagInput);

                    tagInput.value = "";

                    updateHiddenInput();
                });

            });
        </script>

    </body>
</html>

