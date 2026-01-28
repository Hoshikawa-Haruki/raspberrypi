/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.scheduler;

import deu.se.raspberrypi.entity.TempAttachment;
import deu.se.raspberrypi.repository.TempAttachmentRepository;
import deu.se.raspberrypi.service.FileService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author Haruki
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TempAttachmentCleanupScheduler {

    // private static final int TTL_MINUTES = 60; // 60분 초과 시 삭제대상
    private static final int TTL_MINUTES = 10; // 테스트용 (10분 초과 시 삭제 대상)

    private final TempAttachmentRepository tempAttachmentRepository;
    private final FileService fileService;

    @Scheduled(cron = "0 */10 * * * *") // 매 10분마다 실시
    @Transactional
    public void cleanupExpiredTempAttachments() {

        // 삭제대상 임시첨부파일
        // expired 엔티티 객체 리스트
        List<TempAttachment> expired
                = tempAttachmentRepository.findExpiredFiles(TTL_MINUTES);
        log.info("[Scheduler] expired temp files count = {}", expired.size());
        log.info("[Scheduler] start cleanup");

        // 실제 파일 삭제
        for (TempAttachment temp : expired) {
            fileService.deleteTempFile(temp.getUuid(), temp.getExt());
        }

        // DB row 삭제
        tempAttachmentRepository.deleteAll(expired);
    }
}
