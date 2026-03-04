/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.service;

import deu.se.raspberrypi.dto.StoredFileDto;
import deu.se.raspberrypi.entity.Attachment;
import deu.se.raspberrypi.entity.AttachmentType;
import deu.se.raspberrypi.entity.ContentEntity;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Haruki
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AttachmentService {

    private final FileService fileService;

    // 1. 게시글 작성/수정 : 첨부파일 추가 메서드
    public void addFileAttachments(ContentEntity contentEntity, List<MultipartFile> files) {

        if (files == null || files.isEmpty()) {
            return;
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }
            // FileService 호출 → 실제 저장된 파일명 받기// FileService 호출 → 실제 저장된 파일명 받기
            StoredFileDto fileDto = fileService.handleUpload(file);

            if (fileDto == null) {
                continue;
            }

            // 업로드 결과를 엔티티로 변환
            Attachment attachment = new Attachment();
            attachment.setUuid(fileDto.getUuid());
            attachment.setExt(fileDto.getExt());
            attachment.setOriginalName(file.getOriginalFilename());
            attachment.setType(AttachmentType.FILE); // 첨부파일 enum 타입 설정
            contentEntity.addAttachment(attachment); // 양방향 동기화 (cascade로 attachment 테이블에 자동 저장됨)
        }
    }

    // 2. 게시글 수정 : 첨부파일 삭제 메서드
    public void removeAttachmentsById(ContentEntity contentEntity, List<Long> deleteIds) {

        if (deleteIds == null || deleteIds.isEmpty()) {
            return;
        }

        Set<Long> deleteIdSet = new HashSet<>(deleteIds);

        contentEntity.getAttachments().stream() // 첨부파일에 대해서
                .filter(att -> deleteIdSet.contains(att.getId())) // id가 일치하는 파일에 대해서
                .toList() // list로 생성
                .forEach(att -> { // 삭제 수행
                    fileService.deletePhysicalFile(att.getUuid(), att.getExt());
                    contentEntity.removeAttachment(att); // 엔티티 관계 해제
                });
    }
    
    // 3. 게시글 삭제 : 모든 첨부파일 삭제
    public void deleteAllAttachments(ContentEntity contentEntity) {
        for (Attachment att : contentEntity.getAttachments()) {
            fileService.deletePhysicalFile(att.getUuid(), att.getExt());
        }
    }
}
