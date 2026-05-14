const csrf = {
    header: document.querySelector("meta[name='_csrf_header']").content,
    token:  document.querySelector("meta[name='_csrf']").content
};

function adminAction(url, callback) {
    fetch(url, {
        method: "POST",
        headers: { [csrf.header]: csrf.token }
    })
    .then(res => { if (res.ok) callback(); else alert("처리 실패"); })
    .catch(() => alert("오류가 발생했습니다."));
}

function banMember(btn) {
    const id = btn.dataset.id;
    if (!confirm("정지 처리하시겠습니까?")) return;
    adminAction(`/admin/members/${id}/ban`, () => location.reload());
}

function unbanMember(btn) {
    const id = btn.dataset.id;
    if (!confirm("정지를 해제하시겠습니까?")) return;
    adminAction(`/admin/members/${id}/unban`, () => location.reload());
}

function forceWithdraw(btn) {
    const id = btn.dataset.id;
    if (!confirm("강제 탈퇴 처리하시겠습니까? 되돌릴 수 없습니다.")) return;
    adminAction(`/admin/members/${id}/withdraw`, () => location.reload());
}
