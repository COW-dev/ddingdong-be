package ddingdong.ddingdongBE.domain.activityreport.service.dto.query;

import ddingdong.ddingdongBE.domain.activityreport.entity.ActivityReport;
import ddingdong.ddingdongBE.domain.activityreport.entity.Participant;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record ActivityReportQuery(
    Long id,
    LocalDateTime createdAt,
    String name,
    String content,
    String place,
    LocalDateTime startDate,
    LocalDateTime endDate,
    UploadedFileUrlQuery image,
    List<Participant> participants
) {

  public static ActivityReportQuery of(ActivityReport activityReport, UploadedFileUrlQuery image) {
    return ActivityReportQuery.builder()
        .id(activityReport.getId())
        .createdAt(activityReport.getCreatedAt())
        .name(activityReport.getClub().getName())
        .content(activityReport.getContent())
        .place(activityReport.getPlace())
        .startDate(activityReport.getStartDate())
        .endDate(activityReport.getEndDate())
        .image(image)
        .participants(activityReport.getParticipants())
        .build();
  }
}
