/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.service;

import deu.se.raspberrypi.entity.Attachment;
import deu.se.raspberrypi.entity.AttachmentType;
import deu.se.raspberrypi.entity.ContentEntity;
import deu.se.raspberrypi.entity.TempAttachment;
import deu.se.raspberrypi.repository.TempAttachmentRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * 인라인 이미지 승격/삭제 처리 메서드 게시글 본문에 포함된 인라인 이미지의 생명주기를 관리함
 *
 * @author Haruki
 */
@Service
@Transactional
@RequiredArgsConstructor
public class InlineImageService {

    private final TempAttachmentRepository tempAttachmentRepository;
    private final FileService fileService;
    private static final Pattern IMAGE_PATTERN
            = Pattern.compile("/upload(?:_temp)?/([a-zA-Z0-9\\-]+)\\.(png|jpg|jpeg|gif|webp)",
                    Pattern.CASE_INSENSITIVE); // 대소문자 대응

    // 1. 인라인 이미지 UUID 추출 메서드
    public List<String> extractImageUuids(String html) {

        if (html == null) {
            return List.of();
        }
        // /upload/uuid.png
        // /upload_temp/uuid.jpg
        Matcher m = IMAGE_PATTERN.matcher(html);

        List<String> result = new ArrayList<>();
        while (m.find()) {
            result.add(m.group(1)); // uuid
        }
        return result;
    }

    // 2. 인라인 이미지 승격/삭제 메서드
    public void handleInlineImages(ContentEntity contentEntity, String content, Long uploaderId) {

        // 본문에서 인라인 이미지 UUID 추출
        List<String> inlineUuids = extractImageUuids(content);
        // Set으로 만들어 검색 : O(1) 복잡도
        Set<String> inlineUuidSet = new HashSet<>(inlineUuids);

        // uploaderId 기준 temp 이미지 조회
        List<TempAttachment> tempImageList
                = tempAttachmentRepository.findByUploaderId(uploaderId);

        for (TempAttachment temp : tempImageList) {
            if (inlineUuidSet.contains(temp.getUuid())) { // tempImage가 본문에 있으면
                // 승격 처리
                Attachment att = new Attachment();
                att.setUuid(temp.getUuid());
                att.setExt(temp.getExt());
                att.setOriginalName(temp.getOriginalName());
                att.setType(AttachmentType.INLINE);
                contentEntity.addAttachment(att); // FK 설정 + 양방향 동기화
                // 실제 파일 temp → upload 이동 (승격)
                fileService.moveTempToUpload(temp.getUuid(), temp.getExt());
            } else { // 본문에 없으면
                // 미사용 tempImage 삭제
                fileService.deleteTempFile(temp.getUuid(), temp.getExt());
            }
        }

        // 이미지 승격 후, 본문 content 경로 치환
        contentEntity.setContent(
                content.replace("/upload_temp/", "/upload/")
        );

        // temp DB 정리 → 쿼리 1번으로 최적화
        tempAttachmentRepository.deleteAll(tempImageList);
    }

    // 3. 게시글 수정 : 본문(content) 기준 제거된 인라인 이미지(AttachmentType.INLINE) 정리
    public void cleanupInlineImages(ContentEntity contentEntity, String content) {
        Set<String> inlineUuidSet
                = new HashSet<>(extractImageUuids(content)); // 인라인 이미지 셋 추출

        contentEntity.getAttachments().stream() // List<Attachment>를 stream으로 변환
                .filter(att -> att.getType() == AttachmentType.INLINE) // 조건1 : 인라인만
                .filter(att -> !inlineUuidSet.contains(att.getUuid())) // 조건2 : 본문에 없는 uuid만 남김 (삭제대상)
                .toList() // 결과를 새로운 List로 생성 (ConcurrentModification 방지)
                .forEach(att -> { // 해당 리스트를 대상으로 for each 수행
                    fileService.deletePhysicalFile(att.getUuid(), att.getExt()); // 실제 파일 삭제
                    contentEntity.removeAttachment(att);                                  // DB row 삭제(orphanRemoval)
                });
    }
}
