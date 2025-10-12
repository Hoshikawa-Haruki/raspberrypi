/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.controller.legacy;

import deu.se.raspberrypi.util.FilePathResolver;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Haruki
 */
@Controller
@Slf4j
public class DownloadController {

    @Autowired
    private FilePathResolver filePathResolver;

    //@GetMapping("/ch07/download")
    public void download(@RequestParam("filename") String filename,
            HttpServletResponse response) {

        // 업로드된 파일이 저장된 디렉토리 경로 획득
        Path baseDir = filePathResolver.resolveBaseDir();
        Path filePath = baseDir.resolve(filename); // 전체 경로 = baseDir/filename

        // 파일이 존재하지 않을 경우 404 응답 반환
        if (!Files.exists(filePath)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 브라우저가 다운로드로 처리하도록 설정
        response.setContentType("application/octet-stream");
        // 파일 이름에서 UUID 제거 → 원본 파일명만 보여줌
        String originalFileName = filename.substring(filename.indexOf("_") + 1);
        // Content-Disposition 헤더로 "파일 다운로드" 방식 지정
        response.setHeader("Content-Disposition", "attachment; filename=\"" + originalFileName + "\"");
        
        // 실제 파일을 읽어서 응답 스트림에 복사
        try (InputStream in = Files.newInputStream(filePath); OutputStream out = response.getOutputStream()) {

            byte[] buffer = new byte[8192];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        } catch (IOException e) {
            log.error("다운로드 중 오류 발생: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
