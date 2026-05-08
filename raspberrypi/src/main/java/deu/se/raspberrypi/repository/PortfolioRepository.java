/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package deu.se.raspberrypi.repository;

import deu.se.raspberrypi.entity.Portfolio;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Haruki
 */
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    Page<Portfolio> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Portfolio> findByTitleContaining(String keyword, Pageable pageable);

    Page<Portfolio> findByContentContaining(String keyword, Pageable pageable);

    Page<Portfolio> findByAuthorNameSnapshotContaining(String keyword, Pageable pageable);

    @Query("SELECT p FROM Portfolio p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword% OR p.summary LIKE %:keyword%")
    Page<Portfolio> findByTitleOrContentOrSummaryContaining(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
    SELECT p.id, COUNT(c)
    FROM Portfolio p
    LEFT JOIN p.comments c
    WHERE p.id IN :ids
    GROUP BY p.id
    """)
    List<Object[]> countComments(List<Long> ids);
}
