/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package deu.se.raspberrypi.repository;

import deu.se.raspberrypi.entity.PortfolioComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Haruki
 */
public interface PortfolioCommentRepository extends JpaRepository<PortfolioComment, Long> {

    Page<PortfolioComment> findByPortfolioIdOrderByCreatedAtAsc(
            Long portfolioId,
            Pageable pageable
    );
}
