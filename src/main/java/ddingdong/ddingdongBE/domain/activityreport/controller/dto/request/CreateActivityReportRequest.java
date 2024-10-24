package ddingdong.ddingdongBE.domain.activityreport.controller.dto.request;

import ddingdong.ddingdongBE.domain.activityreport.domain.Participant;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.command.CreateActivityReportCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

public record CreateActivityReportRequest(
    @Schema(description = "활동 보고서 회차 정보", example = "1")
    String term,

    @Schema(description = "활동 보고서 내용", example = "세션을 진행하였습니다")
    String content,

    @Schema(description = "활동 장소", example = "S1353")
    String place,

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Schema(description = "활동 시작 일시", example = "2024-01-01 11:11")
    String startDate,

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Schema(description = "활동 종료 일시", example = "2024-01-02 11:11")
    String endDate,

    @Schema(description = "활동보고서 이미지 key", example = "{serverProfile}/{contentType}/2024-01-01/{authId}/{uuid}")
    String imageKey,

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

    public CreateActivityReportCommand toCommand() {
        return CreateActivityReportCommand.builder()
            .term(term)
            .content(content)
            .place(place)
            .imageKey(imageKey)
            .startDate(startDate)
            .endDate(endDate)
            .participants(participants)
            .build();
    }
}
