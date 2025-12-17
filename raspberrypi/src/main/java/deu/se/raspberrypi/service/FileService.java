/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.service;

import deu.se.raspberrypi.dto.StoredFileDto;
import deu.se.raspberrypi.entity.Attachment;
import deu.se.raspberrypi.repository.AttachmentRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 파일 업로드/다운로드 처리를 담당하는 서비스 클래스
 *
 * FilePathResolver를 제거하고 application.properties 설정으로 경로 관리 2025.10.17.
 *
 * @author Haruki
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private String tempUploadDir = "C:/Users/Haruki/Desktop/raspberrypi/upload_temp";

    private final AttachmentRepository attachmentRepository;

    public StoredFileDto handleUpload(MultipartFile file) {
        return storeFile(file, Paths.get(uploadDir));
    }

//    public StoredFileDto handleUpload(MultipartFile upfile) {
//
//        // 1. 파일이 비었으면 null 반환
//        if (upfile == null || upfile.isEmpty()) {
//            log.info("첨부파일이 없음. 업로드 생략");
//            return null;
//        }
//
//        // 2. 저장 경로 지정
//        Path baseDir = Paths.get(uploadDir);
//        try {
//            Files.createDirectories(baseDir);
//        } catch (IOException e) {
//            log.error("업로드 디렉터리 생성 실패: {}", e.getMessage());
//            throw new RuntimeException("업로드 디렉터리 생성 실패", e);
//        }
//
//        // 3. UUID 생성, 확장자 추출
//        String originalName = StringUtils.cleanPath(upfile.getOriginalFilename());
//        if (originalName.contains("..")) {
//            throw new IllegalArgumentException("유효하지 않은 파일명입니다.");
//        }
//        String ext = StringUtils.getFilenameExtension(originalName); // 1) 확장자 추출
//        String uuid = UUID.randomUUID().toString(); // 2) uuid 생성
//        String savedName = uuid + "." + ext; // 3) 파일명 생성
//        Path dest = baseDir.resolve(savedName); // 4) 파일이 저장될 전체 경로 생성
//
//        // 4. 파일 저장
//        try {
//            upfile.transferTo(dest);
//            log.info("파일 저장 완료: {}", dest.toAbsolutePath());
//            // DTO로 결과 반환
//            StoredFileDto fileDto = new StoredFileDto();
//            fileDto.setUuid(uuid);
//            fileDto.setExt(ext);
//            fileDto.setOriginalName(originalName);
//            return fileDto;
//        } catch (IOException e) {
//            log.error("파일 저장 실패: {}", e.getMessage());
//            throw new RuntimeException("파일 저장 실패", e);
//        }
//    }
    public void handleDownload(String uuid, HttpServletResponse response) {

        // 1. UUID 로 DB 조회
        Attachment att = attachmentRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("파일이 존재하지 않습니다."));

        String ext = att.getExt();
        String originalName = att.getOriginalName();

        // 2. 다운로드 경로
        Path filePath = Paths.get(uploadDir).resolve(uuid + "." + ext);
        if (!Files.exists(filePath)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        // 3. MIME 추정 (없으면 바이너리[기본값]로 처리)
        String contentType;
        try {
            contentType = Files.probeContentType(filePath); // 1) 파일타입 추측 
        } catch (IOException e) {
            contentType = null;
        }
        response.setContentType(contentType != null ? contentType : "application/octet-stream"); // 2) 못 알아내면 기본값(다운로드용)
        // 3) 브라우저에 타입 전달

        // 4. 파일명 인코딩
        String encodedName = URLEncoder.encode(originalName, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=\"" + encodedName + "\"; filename*=UTF-8''" + encodedName
        );

        // 5. 파일 전송 스트림
        try (InputStream in = Files.newInputStream(filePath); OutputStream out = response.getOutputStream()) {

            byte[] buffer = new byte[8192];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }

        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public void deletePhysicalFile(String uuid, String ext) {
        Path path = Paths.get(uploadDir, uuid + "." + ext);

        try {
            Files.deleteIfExists(path);
            log.info("삭제 완료: {}", path);
        } catch (IOException e) {
            log.error("파일 삭제 실패: {}", path, e);
        }
    }

    // TODO 
    // - 하드코딩 제거
    // - 파일 이동/삭제를 비동기로 처리
    // 2025.12.11.
    public void deleteTempFile(String uuid, String ext) {
        Path path = Paths.get("C:/Users/Haruki/Desktop/raspberrypi/upload_temp", uuid + "." + ext);
        try {
            Files.deleteIfExists(path);
            log.info("삭제 완료: {}", path);
        } catch (IOException e) {
            log.error("파일 삭제 실패: {}", path, e);
        }
    }

    public void moveTempToUpload(String uuid, String ext) {

        // temp 실제 경로
        Path tempFile = Paths.get("C:/Users/Haruki/Desktop/raspberrypi/upload_temp",
                uuid + "." + ext);

        // upload 실제 경로
        Path uploadFile = Paths.get("C:/Users/Haruki/Desktop/raspberrypi/upload",
                uuid + "." + ext);

        try {
            // upload 폴더 없으면 생성
            Files.createDirectories(uploadFile.getParent());

            Files.move(tempFile, uploadFile, StandardCopyOption.REPLACE_EXISTING);

            log.info("[FILE] Temp → Upload 이동 성공: {} → {}",
                    tempFile.toAbsolutePath(), uploadFile.toAbsolutePath());

        } catch (IOException e) {
            log.error("[FILE] Temp → Upload 파일 이동 실패: {}", e.getMessage());
            throw new RuntimeException("파일 이동 실패", e);
        }
    }

    public StoredFileDto handleTempUpload(MultipartFile file) {
        return storeFile(file, Paths.get(tempUploadDir));
    }

    private StoredFileDto storeFile(MultipartFile upfile, Path baseDir) {

        // 1. 파일이 비었다면 null 반환
        if (upfile == null || upfile.isEmpty()) {
            log.info("첨부파일이 없음. 업로드 생략");
            return null;
        }

        // 2. 저장 경로 준비
        try {
            Files.createDirectories(baseDir);
        } catch (IOException e) {
            log.error("업로드 디렉터리 생성 실패: {}", e.getMessage());
            throw new RuntimeException("디렉터리 생성 실패", e);
        }

        // 3. UUID 생성, 확장자 추출
        String originalName = StringUtils.cleanPath(upfile.getOriginalFilename());
        if (originalName.contains("..")) {
            throw new IllegalArgumentException("유효하지 않은 파일명입니다.");
        }
        String ext = StringUtils.getFilenameExtension(originalName);
        String uuid = UUID.randomUUID().toString();
        Path dest = baseDir.resolve(uuid + "." + ext); // 파일이 저장될 전체 경로 생성

        // 4. 파일 저장
        try {
            upfile.transferTo(dest);
            log.info("파일 저장 완료: {}", dest.toAbsolutePath());
            return new StoredFileDto(uuid, ext, originalName);
        } catch (IOException e) {
            log.error("파일 저장 실패: {}", e.getMessage());
            throw new RuntimeException("파일 저장 실패", e);
        }
    }
}
