/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package deu.se.raspberrypi.repository;

import deu.se.raspberrypi.entity.IdempotencyKey;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Haruki
 */
public interface IdempotencyKeyRepository extends JpaRepository<IdempotencyKey, Long> {

    @Modifying // 데이터 변경 쿼리
    @Query("delete from IdempotencyKey k where k.createdAt < :cutoff")
    int deleteExpiredIdemKeys(@Param("cutoff") LocalDateTime cutoff);

}
