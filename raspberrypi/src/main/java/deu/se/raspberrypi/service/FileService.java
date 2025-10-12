/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.service;

import deu.se.raspberrypi.util.FilePathResolver;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Haruki
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final FilePathResolver filePathResolver;

    public String handleUpload(String username, MultipartFile upfile, Model model) {
        // 1. 기본 검증
        if (!StringUtils.hasText(username) || upfile == null || upfile.isEmpty()) {
            return String.format("username(%s)이 없거나 업로드 파일이 지정되지 않았습니다.", username);
        }

        // 2. 저장 경로 설정
        Path baseDir = filePathResolver.resolveBaseDir();
        if (baseDir == null) {
            return "서버 저장 경로를 찾을 수 없습니다.";
        }
        try {
            Files.createDirectories(baseDir);
        } catch (IOException e) {
            log.error("업로드 디렉터리 생성 실패: {}", e.getMessage());
            return "업로드 디렉터리 생성에 실패했습니다.";
        }

        // 3. 파일명 정규화 + UUID 프리픽스
        String original = StringUtils.cleanPath(upfile.getOriginalFilename());
        if (original.contains("..")) {
            return "유효하지 않은 파일명입니다.";
        }

        String savedName = UUID.randomUUID() + "_" + original;
        Path dest = baseDir.resolve(savedName);

        // 4. 실제 저장
        try {
            upfile.transferTo(dest);
            log.info("파일 저장 완료: {}", dest.toAbsolutePath());
            // ✅ jsp에 다운로드 링크 전달
            model.addAttribute("downloadFile", savedName);
            return String.format("username = %s, %s 저장 완료", username, original);
        } catch (IOException e) {
            log.error("파일 저장 실패: {}", e.getMessage());
            return "파일 저장 중 오류가 발생했습니다.";
        }
    }

    public void handleDownload(String filename, HttpServletResponse response) {

        // 업로드된 파일이 저장된 디렉토리 경로 획득
        Path baseDir = filePathResolver.resolveBaseDir();
        Path filePath = baseDir.resolve(filename);
        
        log.debug("다운로드 요청: filename={}", filename);

        // 파일이 존재하지 않을 경우 404 응답 반환
        if (!Files.exists(filePath)) {
            log.warn("다운로드 실패 - 파일 없음: {}", filePath);
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

            log.debug("다운로드 시작: {}", filePath.toAbsolutePath());
            byte[] buffer = new byte[8192];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            log.info("다운로드 완료: {}", originalFileName);

        } catch (IOException e) {
            log.error("다운로드 중 오류 발생: {}", e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
