/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.scheduler;

import deu.se.raspberrypi.repository.IdempotencyKeyRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author Haruki
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class IdempotencyCleanupScheduler {

    // 보관 기간 (운영 기준: 24시간)
    private static final int TTL_HOURS = 24;

    private final IdempotencyKeyRepository idempotencyKeyRepository;

    @Scheduled(cron = "0 0 4 * * *") // 10초마다 실행
    @Transactional
    public void cleanupExpiredIdempotencyKeys() {

        LocalDateTime cutoff = LocalDateTime.now().minusHours(TTL_HOURS);

        int deleted = idempotencyKeyRepository
                .deleteExpiredIdemKeys(cutoff); // cutoff 기준 만료된 키 삭제

        log.info("[Scheduler] Idempotency cleanup done. deleted={}, cutoff={}",
                deleted, cutoff);
    }
}
