/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.dto;

import deu.se.raspberrypi.entity.Member;
import java.time.LocalDateTime;
import lombok.Getter;

/**
 *
 * @author Haruki
 */
@Getter
public class MemberProfileDto {

    private final String nickname;
    private final String email;
    private final LocalDateTime createdAt;

    public MemberProfileDto(String nickname,
            String email,
            LocalDateTime createdAt) {
        this.nickname = nickname;
        this.email = email;
        this.createdAt = createdAt;
    }

    // 엔티티 → DTO 변환 메서드
    public static MemberProfileDto from(Member member) {
        return new MemberProfileDto(
                member.getNickname(),
                member.getEmail(),
                member.getCreatedAt()
        );
    }
}
