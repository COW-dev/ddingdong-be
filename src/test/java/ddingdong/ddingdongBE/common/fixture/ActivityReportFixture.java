package ddingdong.ddingdongBE.common.fixture;

import ddingdong.ddingdongBE.domain.activityreport.entity.ActivityReport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import java.time.LocalDateTime;
import java.util.List;

public class ActivityReportFixture {

    public static ActivityReport create(Club club, int term) {
        return ActivityReport.builder()
                .term(term)
                .content("세션을 진행하였습니다")
                .place("S1353")
                .startDate(LocalDateTime.of(2026, 3, 20, 10, 0))
                .endDate(LocalDateTime.of(2026, 3, 20, 12, 0))
                .participants(List.of())
                .club(club)
                .build();
    }
}
