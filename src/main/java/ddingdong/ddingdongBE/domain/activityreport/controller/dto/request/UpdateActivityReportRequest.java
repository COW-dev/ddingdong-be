package ddingdong.ddingdongBE.domain.activityreport.controller.dto.request;

import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReport;
import ddingdong.ddingdongBE.domain.activityreport.domain.Participant;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

public record UpdateActivityReportRequest(
    @Schema(description = "내용", example = "활동보고서 내용입니다")
    String content,

    @Schema(description = "활동 장소", example = "S1353")
    String place,

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜는 yyyy-MM-dd 형식이어야 합니다.")
    @Schema(description = "활동 시작 일자", example = "2024-01-02")
    String startDate,

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜는 yyyy-MM-dd 형식이어야 합니다.")
    @Schema(description = "활동 종료 일자", example = "2024-01-04")
    String endDate,

    @Schema(description = "활동 참여자 목록",
        example = """
            [{
            "name" : "홍길동",
            "studentId" : "1",
            "department" : "서부서"
            }]
           """)
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
