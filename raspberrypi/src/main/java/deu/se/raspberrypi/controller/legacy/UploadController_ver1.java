/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.controller.legacy;

import jakarta.servlet.ServletContext;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Haruki
 * 교수님 코드
 */
@Controller
@Slf4j
public class UploadController_ver1 {
    
    @Autowired
    private ServletContext ctx;

    //@GetMapping("/ch07/upload")
    public String upload() {
        return "ch07/upload/index";
    }
    
    //@PostMapping("/ch07/upload.do")
    public String uploadDo(@RequestParam String username, @RequestParam MultipartFile upfile, Model model) {
        log.debug("upload.do: username = {}, upfile = (), File.separator= {}",
                username, upfile.getOriginalFilename(), File.separator);
        // 파일 저장. /target 폴더에 생성됨
        // apache tomcat 의 경우 서버 배포 시 WEB-INF/upload 폴더에 생성됨
        // 일반적으로 배포되서 실행이 될때, web-inf에 있는 파일들은 외부에서 열람 불가. tomcat 같은 jsp 컨테이너만 볼 수 있음
        // 보안 측면에서 web-inf 내 폴더를 만들어서 작업하면 보다 안전함
        String basePath = ctx.getRealPath("/WEB-INF") + File.separator + "upload"; // 파일 저장경로 생성
        
        log.debug("upload.do: basePath = {}", basePath);
        File baseDir = new File(basePath);
        if(!baseDir.exists()) { // 파일 저장경로 없을 시, 폴더 생성
            baseDir.mkdir();
        }
        if("".equals(username) || "".equals(upfile.getOriginalFilename())) { // 유저네임이나 파일이 없을 경우
            log.debug("username = \"{}\", upfile=\"{}\"", username, upfile.getOriginalFilename());
            model.addAttribute("exec_message",
                    String.format("username(%s)이 없거나 upload 파일(%s) 지정이 되지 않았습니다.",
                            username, upfile.getOriginalFilename()));
        } else {
            log.debug("upfile=\"{}\"", upfile.getOriginalFilename());
            File f = new File(basePath + File.separator + upfile.getOriginalFilename()); // 파일 을 만듬? (이해 불가)
            try(BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f))){ // 데코레이터 패턴
                bos.write(upfile.getBytes()); // 업로드 된 것을 모두 읽어들임. 객체 생성
            } catch (IOException e) {
                log.error("upload.do: 오류 발생 - {}", e.getMessage());
            }
            model.addAttribute("exec_message",
                    String.format("username = %s, %s 파일 저장이 잘 되었습니다.",
                            username, upfile.getOriginalFilename()));
        }
        return "ch07/upload/index";
    }
}
