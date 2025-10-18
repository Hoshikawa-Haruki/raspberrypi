/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.util;

import jakarta.servlet.ServletContext;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * application.properties로 경로 지정 후, 클래스에서 @Value 형태로 사용
 * legacy 행. 사용 X
 * 
 * 2025.10.17.
 * @author Haruki
 */
@Component
@RequiredArgsConstructor
public class FilePathResolver {

    // Autowired 와 동일
    private final ServletContext ctx;

    // 설정 파일에 없으면 빈 문자열로 주입
    @Value("${file.upload.dir:}")
    private String configuredUploadDir;


    public Path resolveBaseDir() {
        // application.properties에서 file.upload.dir 설정값을 읽어옴.
        // 값이 없으면 빈 문자열("")로 기본 주입됨.
        if (StringUtils.hasText(configuredUploadDir)) {
            return Paths.get(configuredUploadDir);
        }

        // 설정이 없으면 기본 경로로 fallback → WEB-INF/upload
        // 실제 업로드 파일이 저장될 디렉터리의 절대경로를 반환
        String root = ctx.getRealPath("/WEB-INF");
        if (root == null) { // 내장 톰캣(JAR 실행) 등에서는 null을 반환할 수 있음
            return null;
        }

        return Paths.get(root, "upload");
    }
}
