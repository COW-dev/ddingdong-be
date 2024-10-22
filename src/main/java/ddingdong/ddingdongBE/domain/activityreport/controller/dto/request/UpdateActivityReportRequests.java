package ddingdong.ddingdongBE.domain.activityreport.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "UpdateActivityReportRequests", description = "활동보고서 수정 요청")
public record UpdateActivityReportRequests(
    @Schema(description = "요청된 활동보고서들", implementation = UpdateActivityReportRequest.class)
    List<UpdateActivityReportRequest> activityReportRequests
) {
}
