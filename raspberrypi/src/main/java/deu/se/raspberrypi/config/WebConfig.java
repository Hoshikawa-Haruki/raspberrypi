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
 * WebConfig
 *
 * Spring MVC 설정을 커스터마이징하기 위한 구성 클래스.
 * ResourceHandler 등을 등록하여 정적 리소스 매핑을 확장할 때 사용한다.
 *
 * 2025.10.28
 * @author Haruki
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**") // upload로 시작하는 모든 url 요청 처리
                .addResourceLocations("file:" + uploadDir + "/"); // 해당 요청을 어떤 실제 경로에서 찾을지 지정
    }
}
