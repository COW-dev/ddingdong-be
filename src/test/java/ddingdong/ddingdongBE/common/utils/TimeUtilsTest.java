package ddingdong.ddingdongBE.common.utils;

import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TimeUtilsTest {

    @DisplayName("현재 날짜가 기간 내 포함된다면 true를 반환한다.")
    @Test
    void isDateInRange() {
        // given
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.minusDays(1);
        LocalDate endDate = now.plusDays(1);
        // when
        boolean isActive = TimeUtils.isDateInRange(now, startDate, endDate);
        // then
        Assertions.assertThat(isActive).isTrue();
    }
}
