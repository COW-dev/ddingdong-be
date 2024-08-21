package ddingdong.ddingdongBE.domain.scorehistory.entity;

import static ddingdong.ddingdongBE.common.exception.InvalidatedMappingException.InvalidatedEnumValue;

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
                .isInstanceOf(InvalidatedEnumValue.class)
                .hasMessage(ErrorMessage.ILLEGAL_SCORE_CATEGORY.getText());
    }

}
