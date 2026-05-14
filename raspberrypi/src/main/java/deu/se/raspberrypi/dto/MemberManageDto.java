package deu.se.raspberrypi.dto;

/**
 * 관리자 페이지용 회원 정보 DTO
 * Member 엔티티에서 관리자가 필요한 필드만 추출하여 전달
 *
 * 2026.05.14.
 *
 * @author Haruki
 */

import deu.se.raspberrypi.entity.Member;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class MemberManageDto {

    private final Long memberId;
    private final String email;
    private final String nickname;
    private final String status;
    private final String role;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime deletedAt;

    private MemberManageDto(Member m) {
        this.memberId = m.getId();
        this.email = m.getEmail();
        this.nickname = m.getNickname();
        this.status = m.getStatus();
        this.role = m.getRole();
        this.createdAt = m.getCreatedAt();
        this.updatedAt = m.getUpdatedAt();
        this.deletedAt = m.getDeletedAt();
    }

    public static MemberManageDto from(Member m) {
        return new MemberManageDto(m);
    }
}
