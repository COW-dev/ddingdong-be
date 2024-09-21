package ddingdong.ddingdongBE.domain.activityreport.controller.dto.request;

import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReport;
import ddingdong.ddingdongBE.domain.activityreport.domain.Participant;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record CreateActivityReportRequest(
    @Schema(description = "활동 보고서 회차 정보", example = "1")
    String term,

    @Schema(description = "활동 보고서 내용", example = "세션을 진행하였습니다")
    String content,

    @Schema(description = "활동 장소", example = "S1353")
    String place,

    @Schema(description = "활동 시작 일시", example = "2024-01-01")
    String startDate,

    @Schema(description = "활동 종료 일시", example = "2024-01-02")
    String endDate,

    @Schema(description = "활동 참여자 명단",
        example = """
            [{
            "name" : "홍길동",
            "studentId" : "1",
            "department" : "서부서"
            }]
           """)
    List<Participant> participants
) {

  public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";

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
