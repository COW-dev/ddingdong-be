package ddingdong.ddingdongBE.domain.activityreport.controller.dto.request;

import ddingdong.ddingdongBE.domain.activityreport.domain.Participant;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.command.UpdateActivityReportCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

public record UpdateActivityReportRequest(
    @Schema(description = "내용", example = "활동보고서 내용입니다")
    String content,

    @Schema(description = "활동 장소", example = "S1353")
    String place,

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Schema(description = "활동 시작 일자", example = "2024-01-02 11:11")
    String startDate,

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Schema(description = "활동 종료 일자", example = "2024-01-04 11:11")
    String endDate,

    @Schema(description = "활동보고서 이미지 key", example = "{serverProfile}/{contentType}/2024-01-01/{authId}/{uuid}")
    String key,

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

    public UpdateActivityReportCommand toCommand() {
        return UpdateActivityReportCommand.builder()
            .content(content)
            .place(place)
            .key(key)
            .startDate(startDate)
            .endDate(endDate)
            .participants(participants)
            .build();
    }
}
