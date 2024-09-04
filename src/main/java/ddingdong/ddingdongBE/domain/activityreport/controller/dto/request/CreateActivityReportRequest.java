package ddingdong.ddingdongBE.domain.activityreport.controller.dto.request;

import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReport;
import ddingdong.ddingdongBE.domain.activityreport.domain.Participant;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateActivityReportRequest {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";

    @Schema(description = "활동 보고서 회차 정보")
    private String term;

    @Schema(description = "활동 보고서 내용")
    private String content;

    @Schema(description = "활동 장소")
    private String place;

    @Schema(description = "활동 시작 일시")
    private String startDate;

    @Schema(description = "활동 종료 일시")
    private String endDate;

    @Schema(description = "활동 참여자 명단")
    private List<Participant> participants;

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
