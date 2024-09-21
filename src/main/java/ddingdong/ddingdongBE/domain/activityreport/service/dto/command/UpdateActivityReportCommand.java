package ddingdong.ddingdongBE.domain.activityreport.service.dto.command;

import ddingdong.ddingdongBE.common.utils.TimeUtils;
import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReport;
import ddingdong.ddingdongBE.domain.activityreport.domain.Participant;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record UpdateActivityReportCommand(
    String content,
    String place,
    String startDate,
    String endDate,
    List<Participant> participants
) {


  public ActivityReport toEntity() {
    return ActivityReport.builder()
        .content(content)
        .place(place)
        .startDate(TimeUtils.processDate(startDate, LocalDateTime.now()))
        .endDate(TimeUtils.processDate(endDate, LocalDateTime.now()))
        .participants(participants)
        .build();
  }
}
