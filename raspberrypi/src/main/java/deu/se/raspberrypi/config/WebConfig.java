/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebConfig ResourceHandler 를 등록하여 정적 리소스 매핑을 확장할 때 사용
 *
 * 업로드 파일(static X, 서버 외부 디렉토리)에 대한 정적 리소스 매핑 설정 /upload/** 요청 →
 * file:${file.upload-dir}/ 실제 파일 반환 application.properties 대신 WebMvcConfigurer
 * 사용 (환경변경/배포 유연)
 *
 * 2025.10.28
 *
 * @author Haruki
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 에디터 이미지 첨부 업로드 핸들러
        // 2025.12.20. 현재 사용 X
        registry.addResourceHandler("/upload/**") // upload로 시작하는 모든 url 요청 처리
                .addResourceLocations("file:" + uploadDir + "/"); // 해당 요청을 어떤 실제 경로에서 찾을지 지정
        // upload 요청에 대한 응답은 Spring이 파일을 바로 스트리밍하여 응답
        
        // 임시 업로드 파일
        registry.addResourceHandler("/upload_temp/**")
                .addResourceLocations("file:" + uploadDir + "_temp/");
    }
}
