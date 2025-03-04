package ddingdong.ddingdongBE.domain.activityreport.service.dto.command;

import ddingdong.ddingdongBE.common.utils.TimeUtils;
import ddingdong.ddingdongBE.domain.activityreport.entity.ActivityReport;
import ddingdong.ddingdongBE.domain.activityreport.entity.Participant;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record UpdateActivityReportCommand(
        LocalDateTime now,
        String content,
        String place,
        String startDate,
        String endDate,
        String imageId,
        List<Participant> participants
) {


    public ActivityReport toEntity() {
        return ActivityReport.builder()
                .content(content)
                .place(place)
                .startDate(TimeUtils.processDate(startDate, now))
                .endDate(TimeUtils.processDate(endDate, now))
                .participants(participants)
                .build();
    }
}
