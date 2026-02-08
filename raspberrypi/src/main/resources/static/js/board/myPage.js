/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

let currentMyPostPage = 0;
const MY_POST_PAGE_SIZE = 5;
const MY_POST_BLOCK = 5;

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
    list.innerHTML = "";

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

