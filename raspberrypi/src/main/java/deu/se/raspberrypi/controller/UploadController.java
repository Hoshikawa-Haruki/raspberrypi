/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.controller;

import deu.se.raspberrypi.dto.StoredFileDto;
import deu.se.raspberrypi.entity.Attachment;
import deu.se.raspberrypi.repository.AttachmentRepository;
import deu.se.raspberrypi.service.FileService;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Haruki
 */
@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/upload")
public class UploadController {

    private final FileService fileService;
    private final AttachmentRepository attachmentRepository;

    @PostMapping("/image")
    @ResponseBody
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) {

        log.info("[WYSIWYG] 이미지 업로드 요청: {}", file.getOriginalFilename());

        StoredFileDto dto = fileService.handleUpload(file);

        if (dto == null) {
            return ResponseEntity.badRequest().body("upload failed");
        }

        // 임시 Attachment 엔티티 생성
        Attachment att = new Attachment();
        att.setUuid(dto.getUuid());
        att.setExt(dto.getExt());
        att.setOriginalName(file.getOriginalFilename());
        att.setPost(null); // postID와 연관 아직 X
        attachmentRepository.save(att); // 임시 저장

        String url = "/upload/" + dto.getUuid() + "." + dto.getExt();

        // JSON 데이터 생성. Toast UI의 addImageBlobHook은 JSON을 기대함
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("url", url);

        return ResponseEntity.ok(result);
    }
}
