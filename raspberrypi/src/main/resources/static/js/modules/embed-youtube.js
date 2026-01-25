/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

// src/main/resources/static/js/modules/embed-youtube.js

export function embedYouTube(container) {
    if (!container)
        return;

    const links = container.querySelectorAll("a[href]");

    links.forEach(link => {
        const url = link.href;
        if (!isYouTube(url))
            return;

        const videoId = extractVideoId(url);
        if (!videoId)
            return;

        const wrapper = document.createElement("div");
        wrapper.className = "youtube-wrapper";

        const iframe = document.createElement("iframe");
        iframe.src = `https://www.youtube.com/embed/${videoId}`;
        iframe.setAttribute("frameborder", "0");
        iframe.setAttribute(
                "allow",
                "accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                );
        iframe.allowFullscreen = true;

        wrapper.appendChild(iframe);

        // 링크를 iframe으로 교체
        link.replaceWith(wrapper);
    });
}

function isYouTube(url) {
    return url.includes("youtube.com/watch") || url.includes("youtu.be");
}

function extractVideoId(url) {
    try {
        if (url.includes("youtu.be")) {
            return url.split("/").pop().split("?")[0];
        }
        return new URL(url).searchParams.get("v");
    } catch (e) {
        return null;
    }
}

