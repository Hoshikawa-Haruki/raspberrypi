/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.controller;

import jakarta.servlet.ServletContext;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 *
 * @author Haruki
 */
@Component
@RequiredArgsConstructor
public class FilePathResolver {

    private final ServletContext ctx;

    // 설정 파일에 없으면 빈 문자열로 주입
    @Value("${file.upload.dir:}")
    private String configuredUploadDir;


    public Path resolveBaseDir() {
        // application.properties 설정값이 있으면 그 경로 사용
        if (StringUtils.hasText(configuredUploadDir)) {
            return Paths.get(configuredUploadDir);
        }

        // 설정이 없으면 기본 경로로 fallback → WEB-INF/upload
        String root = ctx.getRealPath("/WEB-INF");
        if (root == null) {
            return null;
        }

        return Paths.get(root, "upload");
    }
}
