package ddingdong.ddingdongBE.domain.scorehistory.entity;

import static ddingdong.ddingdongBE.common.exception.ErrorMessage.*;

import lombok.Getter;

@Getter
public enum ScoreCategory {
    CLEANING("청소"),
    ACTIVITY_REPORT("동아리 활동 보고서"),
    LEADER_CONFERENCE("전동대회"),
    BUSINESS_PARTICIPATION("총동연 사업 참여"),
    BONUS("가산점"),
    DEDUCTION("감점");

    private final String category;

    ScoreCategory(String category) {
        this.category = category;
    }

    public static ScoreCategory of(String category) {
        for (ScoreCategory scoreCategory : ScoreCategory.values()) {
            if (scoreCategory.category.equalsIgnoreCase(category)) {
                return scoreCategory;
            }
        }

        throw new IllegalArgumentException(ILLEGAL_SCORE_CATEGORY.getText());
    }
}
