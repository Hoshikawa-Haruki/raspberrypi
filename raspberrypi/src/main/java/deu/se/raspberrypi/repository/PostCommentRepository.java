/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package deu.se.raspberrypi.repository;

import deu.se.raspberrypi.entity.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Haruki
 */
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    Page<PostComment> findByPostIdOrderByCreatedAtAsc(
            Long postId,
            Pageable pageable
    );
}
