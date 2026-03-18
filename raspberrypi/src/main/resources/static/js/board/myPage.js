/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

let currentMyPostPage = 0;
const MY_POST_PAGE_SIZE = 5;
const MY_POST_BLOCK = 5;

const csrfToken = document.querySelector('meta[name="_csrf"]').content;
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

document.addEventListener("DOMContentLoaded", () => {
    loadMyPosts();
});

async function loadMyPosts(page = 0) {
    const res = await fetch(`/api/myposts?page=${page}&size=${MY_POST_PAGE_SIZE}`);
    const data = await res.json();

    // 게시글 개수 카운트
    document.getElementById("my-post-count").innerText =
            data.totalElements;

    const list = document.getElementById("my-post-list");
    const empty = document.getElementById("my-post-empty");
    const pagination = document.getElementById("my-post-pagination");

    list.innerHTML = "";

    // 🔹 게시글 없음
    if (data.content.length === 0) {
        empty.style.display = "block";
        pagination.style.display = "none";
        return;
    }

    // 🔹 게시글 있음
    empty.style.display = "none";
    pagination.style.display = "flex";

    data.content.forEach(post => {
        const commentHtml = post.commentCount > 0
                ? `<span class="comment-count">[${post.commentCount}]</span>`
                : '';

        list.innerHTML += `
            <li>
                <a href="/board/view/${post.id}" class="post-title">
                    <span class="title-text">${post.title}</span>
                    ${commentHtml}
                </a>
                <span class="post-date">${post.formattedCreatedAt}</span>
        
                <button class="post-delete-btn" data-id="${post.id}" type="button" aria-label="삭제">
                    <img src="/images/delete2.svg" alt="삭제">
                </button>
            </li>
        `;
    });

    renderMyPostPagination(data);
    currentMyPostPage = page;
}

function renderMyPostPagination(data) {
    const container = document.getElementById("my-post-pagination");
    container.innerHTML = "";

    const current = data.number;
    const total = data.totalPages;

    const start = Math.floor(current / MY_POST_BLOCK) * MY_POST_BLOCK;
    const end = Math.min(start + MY_POST_BLOCK, total);

    if (start > 0) {
        container.appendChild(createMyPostBtn("‹", start - 1));
    }

    for (let i = start; i < end; i++) {
        const btn = createMyPostBtn(i + 1, i);
        if (i === current)
            btn.classList.add("active");
        container.appendChild(btn);
    }

    if (end < total) {
        container.appendChild(createMyPostBtn("›", end));
    }
}

function createMyPostBtn(label, page) {
    const btn = document.createElement("button");
    btn.textContent = label;
    btn.onclick = () => loadMyPosts(page);
    return btn;
}


// ===============================
// 전역 이벤트 리스너
// ===============================

document.addEventListener("click", async (e) => {

    const btn = e.target.closest(".post-delete-btn");
    if (!btn)
        return;

    e.stopPropagation();

    const postId = btn.dataset.id;

    if (!confirm("정말 삭제하시겠습니까?"))
        return;

    try {
        const res = await fetch(`/api/posts/${postId}`, {
            method: "DELETE",
            headers: {
                [csrfHeader]: csrfToken
            }
        });
        if (!res.ok)
            throw new Error();

        const li = btn.closest("li");
        li.remove();

        // 카운트 감소
        const countEl = document.getElementById("my-post-count");
        countEl.innerText = parseInt(countEl.innerText) - 1;

    } catch (err) {
        alert("삭제 실패");
        console.error(err);
    }
});
