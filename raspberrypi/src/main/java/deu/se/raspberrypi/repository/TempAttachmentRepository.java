/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package deu.se.raspberrypi.repository;

import deu.se.raspberrypi.entity.TempAttachment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Haruki
 */
public interface TempAttachmentRepository extends JpaRepository<TempAttachment, Long> {

    // 게시글 수정/삭제 시 tempUpload 관련 메서드
    List<TempAttachment> findByUploaderId(Long uploaderId);

    // 삭제대상 임시첨부파일 조회
    @Query(
            value = """
        SELECT * FROM attachment_temp
        WHERE created_at < (NOW() - INTERVAL :ttl MINUTE)
        """,
            nativeQuery = true
    )
    List<TempAttachment> findExpiredFiles(@Param("ttl") int ttlMinutes);
}
