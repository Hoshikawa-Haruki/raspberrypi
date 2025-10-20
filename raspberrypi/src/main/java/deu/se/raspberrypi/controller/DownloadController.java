/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.controller;

import deu.se.raspberrypi.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 다운로드 url 요청을 처리하는 컨트롤러
 * 
 * 2025.10.19.
 * @author Haruki
 */
@Controller
@RequiredArgsConstructor
public class DownloadController {

    private final FileService fileService;

    @GetMapping("/download/{uuid}")
    public void download(@PathVariable String uuid, HttpServletResponse response) {
        fileService.handleDownload(uuid, response);
    }

}
