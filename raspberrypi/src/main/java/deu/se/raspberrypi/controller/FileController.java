/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.controller;

import deu.se.raspberrypi.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Haruki
 */
@Controller
@Slf4j
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/ch07/upload")
    public String uploadForm() {
        return "ch07/upload/index";
    }

    @PostMapping("/ch07/upload.do")
    public String uploadFile(
            @RequestParam String username,
            @RequestParam("upfile") MultipartFile upfile,
            Model model) {

        String message = fileService.handleUpload(username, upfile, model);
        model.addAttribute("exec_message", message);
        return "ch07/upload/index";
    }
    
    @GetMapping("/ch07/download")
    public void downloadFile(@RequestParam("filename") String filename,
                             HttpServletResponse response) {
        fileService.handleDownload(filename, response);
    }
}