package ddingdong.ddingdongBE.domain.activityreport.controller.dto.request;

import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReport;
import ddingdong.ddingdongBE.domain.activityreport.domain.Participant;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record UpdateActivityReportRequest(
    @Schema(description = "내용")
    String content,

    @Schema(description = "활동 장소")
    String place,

    @Schema(description = "활동 시작 일자")
    String startDate,

    @Schema(description = "활동 종료 일자")
    String endDate,

    @Schema(description = "활동 참여자 목록")
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
