/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package deu.se.raspberrypi.repository;

import deu.se.raspberrypi.dto.MyPostDto;
import deu.se.raspberrypi.dto.PostListDto;
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

    // 1. 마이페이지 작성글
    @Query(
        value = """
        SELECT new deu.se.raspberrypi.dto.MyPostDto(
            p.id, p.title, p.createdAt, COUNT(c)
        )
        FROM Post p
        LEFT JOIN p.comments c
        WHERE p.authorId.id = :authorId
        GROUP BY p.id, p.title, p.createdAt
        ORDER BY p.createdAt DESC
        """,
        countQuery = "SELECT COUNT(p) FROM Post p WHERE p.authorId.id = :authorId"
    )
    Page<MyPostDto> findMyPostsDto(
            @Param("authorId") Long authorId,
            Pageable pageable
    );

    // 전체 목록
    @Query(
        value = """
        SELECT new deu.se.raspberrypi.dto.PostListDto(
            p.id, p.title, p.authorNameSnapshot, p.ipAddress, p.createdAt, COUNT(c)
        )
        FROM Post p
        LEFT JOIN p.comments c
        GROUP BY p.id, p.title, p.authorNameSnapshot, p.ipAddress, p.createdAt
        ORDER BY p.id DESC
        """,
        countQuery = "SELECT COUNT(p) FROM Post p"
    )
    Page<PostListDto> findPostList(Pageable pageable);

    // 제목 검색
    @Query(
        value = """
        SELECT new deu.se.raspberrypi.dto.PostListDto(
            p.id, p.title, p.authorNameSnapshot, p.ipAddress, p.createdAt, COUNT(c)
        )
        FROM Post p
        LEFT JOIN p.comments c
        WHERE p.title LIKE %:keyword%
        GROUP BY p.id, p.title, p.authorNameSnapshot, p.ipAddress, p.createdAt
        ORDER BY p.id DESC
        """,
        countQuery = "SELECT COUNT(p) FROM Post p WHERE p.title LIKE %:keyword%"
    )
    Page<PostListDto> searchByTitle(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    // 내용 검색
    @Query(
        value = """
        SELECT new deu.se.raspberrypi.dto.PostListDto(
            p.id, p.title, p.authorNameSnapshot, p.ipAddress, p.createdAt, COUNT(c)
        )
        FROM Post p
        LEFT JOIN p.comments c
        WHERE p.content LIKE %:keyword%
        GROUP BY p.id, p.title, p.authorNameSnapshot, p.ipAddress, p.createdAt
        ORDER BY p.id DESC
        """,
        countQuery = "SELECT COUNT(p) FROM Post p WHERE p.content LIKE %:keyword%"
    )
    Page<PostListDto> searchByContent(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    // 작성자 검색
    @Query(
        value = """
        SELECT new deu.se.raspberrypi.dto.PostListDto(
            p.id, p.title, p.authorNameSnapshot, p.ipAddress, p.createdAt, COUNT(c)
        )
        FROM Post p
        LEFT JOIN p.comments c
        WHERE p.authorNameSnapshot LIKE %:keyword%
        GROUP BY p.id, p.title, p.authorNameSnapshot, p.ipAddress, p.createdAt
        ORDER BY p.id DESC
        """,
        countQuery = "SELECT COUNT(p) FROM Post p WHERE p.authorNameSnapshot LIKE %:keyword%"
    )
    Page<PostListDto> searchByWriter(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    // 제목 + 내용 검색
    @Query(
        value = """
        SELECT new deu.se.raspberrypi.dto.PostListDto(
            p.id, p.title, p.authorNameSnapshot, p.ipAddress, p.createdAt, COUNT(c)
        )
        FROM Post p
        LEFT JOIN p.comments c
        WHERE p.title LIKE %:keyword%
           OR p.content LIKE %:keyword%
        GROUP BY p.id, p.title, p.authorNameSnapshot, p.ipAddress, p.createdAt
        ORDER BY p.id DESC
        """,
        countQuery = """
        SELECT COUNT(p) FROM Post p
        WHERE p.title LIKE %:keyword%
           OR p.content LIKE %:keyword%
        """
    )
    Page<PostListDto> searchByTitleOrContent(
            @Param("keyword") String keyword,
            Pageable pageable
    );
}
