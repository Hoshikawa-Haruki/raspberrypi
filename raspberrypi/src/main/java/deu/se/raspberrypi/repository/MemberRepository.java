/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.repository;

import deu.se.raspberrypi.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Haruki
 */

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email); // 로그인
    boolean existsByEmail(String email); // 중복가입 체크

    long countByStatus(String status);

    Page<Member> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Member> findByStatusInOrderByUpdatedAtDesc(List<String> statuses, Pageable pageable);

    Page<Member> findByStatusOrderByCreatedAtDesc(String status, Pageable pageable);
}
