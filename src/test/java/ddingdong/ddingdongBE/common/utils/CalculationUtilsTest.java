package ddingdong.ddingdongBE.common.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CalculationUtilsTest {

    @DisplayName("beforeCount기준으로 beforeCount와 count의 증감율을 구한다.")
    @Test
    void calculateDifferenceRatio() {
        // given
        int beforeCount = 30;
        int count = 45;

        int beforeCount2 = 45;
        int count2 = 30;
        // when
        int result = CalculationUtils.calculateDifferenceRatio(beforeCount, count);
        int result2 = CalculationUtils.calculateDifferenceRatio(beforeCount2, count2);
        // then
        assertThat(result).isEqualTo(50);
        assertThat(result2).isEqualTo(-33);
    }
}
