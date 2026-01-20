/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package deu.se.raspberrypi.repository;

import deu.se.raspberrypi.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Haruki
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // 기본 CRUD 메서드 (save, findById, findAll, deleteById) 자동 제공

    // 1. 마이페이지 내 작성글
    Page<Post> findByAuthorId_Id(Long authorId, Pageable pageable);

    // 제목
    @Query("""
        select p
        from Post p
        where p.title like %:keyword%
    """)
    Page<Post> searchByTitle(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    // 내용
    @Query("""
        select p
        from Post p
        where p.content like %:keyword%
    """)
    Page<Post> searchByContent(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    // 작성자
    @Query("""
        select p
        from Post p
        where p.authorNameSnapshot like %:keyword%
    """)
    Page<Post> searchByWriter(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    // 제목 + 내용
    @Query("""
        select p
        from Post p
        where p.title like %:keyword%
           or p.content like %:keyword%
    """)
    Page<Post> searchByTitleOrContent(
            @Param("keyword") String keyword,
            Pageable pageable
    );
}
