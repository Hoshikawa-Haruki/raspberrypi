/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

import { embedYouTube } from "../modules/embed-youtube.js";

document.addEventListener("DOMContentLoaded", () => {
    const content = document.querySelector(".post-content");
    if (!content) return;

    embedYouTube(content);
});


