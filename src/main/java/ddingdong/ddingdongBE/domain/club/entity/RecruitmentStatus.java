package ddingdong.ddingdongBE.domain.club.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecruitmentStatus {

    BEFORE_RECRUIT("모집 예정"),
    RECRUITING("모집중"),
    END_RECRUIT("모집 마감");

    private final String text;
}
