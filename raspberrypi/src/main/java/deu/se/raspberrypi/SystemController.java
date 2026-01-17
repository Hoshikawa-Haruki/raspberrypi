/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Haruki
 */
@Controller
@Slf4j
public class SystemController {

    // 홈페이지 연결
    @GetMapping("/")
    public String home(){
        return "redirect:/board/list";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }
}
