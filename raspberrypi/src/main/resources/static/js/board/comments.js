let currentCommentPage = 0;
const COMMENT_SIZE = 10;
const COMMENT_PAGE_BLOCK = 10;


document.addEventListener("DOMContentLoaded", () => {
    loadComments();

    // 본문 보기 버튼
    document.getElementById("go-title").addEventListener("click", e => {
        e.preventDefault(); // 페이지 이동 막기
        document
                .getElementById("post-container")
                .scrollIntoView({behavior: "smooth"});
    });
    // 댓글 새로고침 버튼
    document.getElementById("comment-refresh")
            .addEventListener("click", (e) => {
                e.preventDefault();   // 페이지 이동 막기
                loadComments(currentCommentPage);       // 댓글만 다시 불러오기
            });
    // 댓글 작성 버튼
    document.getElementById("comment-submit")
            .addEventListener("click", () => {
                const content = document.getElementById("comment-content").value.trim();
                if (!content)
                    return;

                submitComment(content);
                document.getElementById("comment-content").value = "";
            });
    // 댓글 작성 (Enter 지원)
    document.getElementById("comment-content")
            .addEventListener("keydown", e => {

                // Enter만 눌렀을 때
                if (e.key === "Enter" && !e.shiftKey) {
                    e.preventDefault(); // 줄바꿈 방지

                    const content = e.target.value.trim();
                    if (!content)
                        return;

                    submitComment(content);
                    e.target.value = "";
                }
            });
});

// 댓글 목록 호출
async function loadComments(page = 0) {
    const res = await fetch(
            `/api/comments?postId=${POST_ID}&page=${page}&size=${COMMENT_SIZE}`
            );
    const data = await res.json();
    const comments = data.content;

    const box = document.getElementById("comment-list");
    box.innerHTML = "";

    comments.forEach(c => {
        box.innerHTML += `
            <div class="comment-item">
                <div class="comment-meta">
                    <span class="comment-author">${c.authorNameSnapshot}</span>
                    <span class="comment-time">${c.formattedCreatedAt}</span>
                    ${c.mine ? `
                        <button class="comment-delete"
                                onclick="deleteComment(${c.id})">×</button>
                    ` : ""}
                </div>
                <div class="comment-content">${c.content}</div>
            </div>
        `;
    });

    document.getElementById("comment-count").innerText =
            data.totalElements;

    renderCommentPagination(data);
    currentCommentPage = page;
}

// 댓글 작성
async function submitComment(content) {
    const res = await fetch("/api/comments", {
        method: "POST",
        credentials: "same-origin", // 세션 인증 핵심
        headers: {
            "Content-Type": "application/json",
            "X-CSRF-TOKEN": CSRF_TOKEN // CSRF 보호
        },
        body: JSON.stringify({
            postId: POST_ID,
            content: content
        })
    });

    if (res.status === 401 || res.status === 403) {
        alert("로그인 후 이용가능 합니다.");
        window.location.href = "/member/loginForm";
        return;
    }

    if (!res.ok) {
        alert("댓글 작성에 실패했습니다.");
        return;
    }

    // 마지막 댓글 페이지로 이동
    await moveToLastCommentPage();
}

async function deleteComment(id) {
    if (!confirm("댓글을 삭제하시겠습니까?")) {
        return; // 취소
    }

    const res = await fetch(`/api/comments/${id}`, {
        method: "DELETE",
        credentials: "same-origin",
        headers: {
            "X-CSRF-TOKEN": CSRF_TOKEN
        }
    });

    if (res.status === 401 || res.status === 403) {
        alert("로그인이 필요합니다.");
        window.location.href = "/member/loginForm";
        return;
    }

    if (!res.ok) {
        alert("댓글 삭제에 실패했습니다.");
        return;
    }

    loadComments(currentCommentPage);
}

// 페이지네이션 렌더링
function renderCommentPagination(data) {
    const container = document.getElementById("comment-pagination");
    container.innerHTML = "";

    const currentPage = data.number;      // 0-based
    const totalPages = data.totalPages;
    
    if (totalPages === 0) return;

    const startPage =
            Math.floor(currentPage / COMMENT_PAGE_BLOCK) * COMMENT_PAGE_BLOCK;
    const endPage =
            Math.min(startPage + COMMENT_PAGE_BLOCK, totalPages);

    // 이전 블록
    if (startPage > 0) {
        container.appendChild(
                createPageButton("‹", startPage - 1)
                );
    }

    // 페이지 번호
    for (let i = startPage; i < endPage; i++) {
        container.appendChild(
                createPageButton(i + 1, i, currentPage)
                );
    }

    // 다음 블록
    if (endPage < totalPages) {
        container.appendChild(
                createPageButton("›", endPage)
                );
    }
}

// 페이지 버튼 생성 
function createPageButton(label, page, currentPage) {
    const btn = document.createElement("button");
    btn.textContent = label;
    btn.classList.add("nav");

    if (page === currentPage) {
        btn.classList.add("active");
        btn.disabled = true;      // 클릭 차단
        return btn;
    }

    btn.onclick = () => loadComments(page);
    return btn;
}

// 마지막 댓글 페이지로 이동
async function moveToLastCommentPage() {
    const res = await fetch(
            `/api/comments?postId=${POST_ID}&page=0&size=${COMMENT_SIZE}`
            );
    const data = await res.json();

    const lastPage = data.totalPages - 1;
    loadComments(lastPage);
}