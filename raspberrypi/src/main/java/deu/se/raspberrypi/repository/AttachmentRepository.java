/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.repository;

import deu.se.raspberrypi.entity.Attachment;
import deu.se.raspberrypi.entity.AttachmentType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Haruki
 */
@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    Optional<Attachment> findByUuid(String uuid);
    
    
    @Query("""
    SELECT a
    FROM Attachment a
    WHERE a.portfolio.id IN :ids
      AND a.type = :type
    """)
    List<Attachment> findPortfolioListThumbnails(List<Long> ids, AttachmentType type);
    // 목록 페이지 썸네일 로드
}
