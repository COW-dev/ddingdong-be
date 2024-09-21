package ddingdong.ddingdongBE.domain.activityreport.service.dto.command;

import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReport;
import ddingdong.ddingdongBE.domain.activityreport.domain.Participant;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.Builder;

@Builder
public record CreateActivityReportCommand(
    String term,
    String content,
    String place,
    String startDate,
    String endDate,
    List<Participant> participants
) {

  private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";

  public ActivityReport toEntity(Club club) {
    LocalDateTime startDateTime = parseToLocalDateTime(startDate);
    LocalDateTime endDateTime = parseToLocalDateTime(endDate);

    return ActivityReport.builder()
        .term(this.term)
        .content(this.content)
        .place(this.place)
        .startDate(startDateTime)
        .endDate(endDateTime)
        .participants(this.participants)
        .club(club)
        .build();
  }

  private LocalDateTime parseToLocalDateTime(String dateString) {
    if (dateString == null || dateString.isBlank()) {
      return null;
    }

    return LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern(DATE_FORMAT));
  }
}
