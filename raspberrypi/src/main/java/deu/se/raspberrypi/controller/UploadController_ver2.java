package deu.se.raspberrypi.controller;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@Slf4j
@RequiredArgsConstructor
public class UploadController_ver2 {
    
    @Autowired
    private FilePathResolver filePathResolver;

    @GetMapping("/ch07/upload")
    public String upload() {
        return "ch07/upload/index";
    }

    @PostMapping("/ch07/upload.do")
    public String uploadDo(@RequestParam String username,
            @RequestParam("upfile") MultipartFile upfile,
            Model model) {

        log.debug("upload.do: username={}, upfile={}, size={}",
                username, (upfile != null ? upfile.getOriginalFilename() : null),
                (upfile != null ? upfile.getSize() : null));

        // 1) 기본 검증
        if (!StringUtils.hasText(username) || upfile == null || upfile.isEmpty()) {
            model.addAttribute("exec_message",
                    String.format("username(%s)이 없거나 업로드 파일이 지정되지 않았습니다.", username));
            return "ch07/upload/index";
        }

        // 2) 저장 루트 결정: 설정값 > WEB-INF/upload(폴백)
        Path baseDir = filePathResolver.resolveBaseDir();
        if (baseDir == null) {
            model.addAttribute("exec_message", "서버 저장 경로를 찾을 수 없습니다.");
            return "ch07/upload/index";
        }
        try {
            Files.createDirectories(baseDir);
        } catch (IOException e) {
            log.error("업로드 디렉터리 생성 실패: {}", e.getMessage());
            model.addAttribute("exec_message", "업로드 디렉터리 생성에 실패했습니다.");
            return "ch07/upload/index";
        }

        // 3) 파일명 정규화 + UUID 프리픽스 (경로 조작 방지)
        String original = StringUtils.cleanPath(upfile.getOriginalFilename());
        if (original.contains("..")) {
            model.addAttribute("exec_message", "유효하지 않은 파일명입니다.");
            return "ch07/upload/index";
        }
        String savedName = UUID.randomUUID() + "_" + original;
        Path dest = baseDir.resolve(savedName);

        // 4) 저장 (transferTo가 더 간단/효율적)
        try {
            upfile.transferTo(dest);
            model.addAttribute("exec_message",
                    String.format("username = %s, %s 저장 완료", username, original));
            log.debug("saved path={}", dest.toAbsolutePath());

            // ✅ jsp에 다운로드 링크 전달
            model.addAttribute("downloadFile", savedName); // ⬅ 추가
            
        } catch (IOException e) {
            log.error("파일 저장 실패: {}", e.getMessage());
            model.addAttribute("exec_message", "파일 저장 중 오류가 발생했습니다.");
        }

        return "ch07/upload/index";
    }
}
