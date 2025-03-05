package ddingdong.ddingdongBE.domain.activityreport.service.dto.query;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import java.util.List;
import lombok.Builder;

@Builder
public record AdminActivityReportListQuery(
        Long clubId,
        String clubName,
        List<ActivityReportInfo> activityReports
) {

    public static AdminActivityReportListQuery of(Club club, List<ActivityReportInfo> activityReportInfos) {
        return AdminActivityReportListQuery.builder()
                .clubId(club.getId())
                .clubName(club.getName())
                .activityReports(activityReportInfos)
                .build();
    }
}
