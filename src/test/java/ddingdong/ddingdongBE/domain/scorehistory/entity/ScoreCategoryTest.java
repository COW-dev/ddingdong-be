package ddingdong.ddingdongBE.domain.scorehistory.entity;

import ddingdong.ddingdongBE.common.exception.ErrorMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ScoreCategoryTest {

    @Test
    void from() {
        //given
        String scoreCategoryName = "CLEAN";

        //when //then
        Assertions.assertThatThrownBy(() -> ScoreCategory.from(scoreCategoryName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.ILLEGAL_SCORE_CATEGORY.getText());
    }

}
