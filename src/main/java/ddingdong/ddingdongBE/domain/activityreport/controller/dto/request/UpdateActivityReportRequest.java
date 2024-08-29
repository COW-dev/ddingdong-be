package ddingdong.ddingdongBE.domain.activityreport.controller.dto.request;

import ddingdong.ddingdongBE.domain.activityreport.domain.Participant;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class UpdateActivityReportRequest {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";

    @Schema(description = "회차 정보")
    private String term;

    @Schema(description = "내용")
    private String content;

    @Schema(description = "활동 장소")
    private String place;

    @Schema(description = "활동 시작 일자")
    private LocalDateTime startDate;

    @Schema(description = "활동 종료 일자")
    private LocalDateTime endDate;

    @Schema(description = "활동 참여자 목록")
    private List<Participant> participants;
}
