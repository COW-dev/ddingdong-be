package ddingdong.ddingdongBE.domain.scorehistory.entity;

import static ddingdong.ddingdongBE.common.exception.ErrorMessage.*;

import ddingdong.ddingdongBE.common.exception.InvalidatedMappingException.InvalidatedEnumValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScoreCategory {
    CLEANING("청소"),
    ACTIVITY_REPORT("동아리 활동 보고서"),
    LEADER_CONFERENCE("전동대회"),
    BUSINESS_PARTICIPATION("총동연 사업 참여"),
    ADDITIONAL("가산점/감점"),
    CARRYOVER_SCORE("점수 이월");

    private final String category;

    public static ScoreCategory from(String category) {
        try {
            return ScoreCategory.valueOf(category);
        } catch (IllegalArgumentException e) {
            throw new InvalidatedEnumValue(ILLEGAL_SCORE_CATEGORY.getText());
        }
    }
}
