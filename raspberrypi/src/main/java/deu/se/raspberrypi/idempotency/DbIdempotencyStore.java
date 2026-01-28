/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.idempotency;

import deu.se.raspberrypi.entity.IdempotencyKey;
import deu.se.raspberrypi.repository.IdempotencyKeyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 * DB 기반 idempotency infra service 클래스
 *
 * @author Haruki
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DbIdempotencyStore implements IdempotencyStore {

    private final IdempotencyKeyRepository repository;

    @Override
    @Transactional
    public boolean tryAcquire(String key) {
        try { // 아이뎀포턴시 키 저장 시도
            repository.save(new IdempotencyKey(key));
            log.info("IDEMPOTENCY ACQUIRED: {}", key);
            return true; // 처음 처리되는 요청
        } catch (DataIntegrityViolationException e) {
            log.info("IDEMPOTENCY DUPLICATED: {}", key);
            return false; // 이미 처리된(중복) 요청
        }
    }
}
