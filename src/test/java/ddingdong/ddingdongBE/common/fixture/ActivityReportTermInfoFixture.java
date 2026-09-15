package ddingdong.ddingdongBE.common.fixture;

import ddingdong.ddingdongBE.domain.activityreport.entity.ActivityReportTermInfo;
import java.time.LocalDate;

public class ActivityReportTermInfoFixture {

    public static ActivityReportTermInfo create(int term, LocalDate startDate, LocalDate endDate) {
        return ActivityReportTermInfo.builder()
                .term(term)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}