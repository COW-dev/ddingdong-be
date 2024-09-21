package ddingdong.ddingdongBE.domain.activityreport.service.dto.command;

import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReport;
import ddingdong.ddingdongBE.domain.activityreport.domain.Participant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        .startDate(processDate(startDate, LocalDateTime.now()))
        .endDate(processDate(endDate, LocalDateTime.now()))
        .participants(participants)
        .build();
  }

  private LocalDateTime processDate(String dateString, LocalDateTime currentDate) {
    if (dateString == null) {
      return currentDate;
    }

    if (dateString.isBlank()) {
      return null;
    }

    return parseToLocalDateTime(dateString);
  }

  private LocalDateTime parseToLocalDateTime(String dateString) {
    return LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
  }
}
