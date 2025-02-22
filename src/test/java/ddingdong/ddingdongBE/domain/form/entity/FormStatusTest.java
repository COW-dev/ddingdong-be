package ddingdong.ddingdongBE.domain.form.entity;

import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FormStatusTest {


    @DisplayName("현재 날짜가 기간 내 포함된다면 알맞는 FormStatus를 반환한다.")
    @Test
    void isDateInRange() {
        // given
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.minusDays(1);
        LocalDate endDate = now.plusDays(1);
        // when
        FormStatus formStatus = FormStatus.getDescription(now, startDate, endDate);
        // then
        Assertions.assertThat(formStatus).isEqualTo(FormStatus.ONGOING);
    }
}
