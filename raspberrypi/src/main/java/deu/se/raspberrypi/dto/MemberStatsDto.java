package deu.se.raspberrypi.dto;

/**
 * 관리자 페이지 회원 현황판용 통계 DTO
 * 전체 / 활성 / 정지 / 탈퇴 회원 수를 담아 뷰에 전달
 *
 * 2026.05.14.
 *
 * @author Haruki
 */
import lombok.Getter;

@Getter
public class MemberStatsDto {

    private final long total;
    private final long active;
    private final long banned;
    private final long deleted;

    public MemberStatsDto(long total, long active, long banned, long deleted) {
        this.total = total;
        this.active = active;
        this.banned = banned;
        this.deleted = deleted;
    }
}
