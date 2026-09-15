package ddingdong.ddingdongBE.domain.activityreport.entity;

import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.common.fixture.ActivityReportTermInfoFixture;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ActivityReportTermInfoTest {

    private static final LocalDate START_DATE = LocalDate.of(2026, 3, 16);
    private static final LocalDate END_DATE = LocalDate.of(2026, 3, 29);

    private final ActivityReportTermInfo termInfo = ActivityReportTermInfoFixture.create(2, START_DATE, END_DATE);

    @DisplayName("오늘이 회차 시작일보다 이전이면 진행 전이다")
    @Test
    void upcomingBeforeStartDate() {
        assertThat(termInfo.getStatus(START_DATE.minusDays(1))).isEqualTo(ActivityReportTermStatus.UPCOMING);
    }

    @DisplayName("오늘이 회차 시작일과 같으면 진행 중이다")
    @Test
    void ongoingOnStartDate() {
        assertThat(termInfo.getStatus(START_DATE)).isEqualTo(ActivityReportTermStatus.ONGOING);
    }

    @DisplayName("오늘이 회차 마감일과 같으면 진행 중이다")
    @Test
    void ongoingOnEndDate() {
        assertThat(termInfo.getStatus(END_DATE)).isEqualTo(ActivityReportTermStatus.ONGOING);
    }

    @DisplayName("오늘이 회차 마감일보다 이후면 진행 종료다")
    @Test
    void closedAfterEndDate() {
        assertThat(termInfo.getStatus(END_DATE.plusDays(1))).isEqualTo(ActivityReportTermStatus.CLOSED);
    }
}