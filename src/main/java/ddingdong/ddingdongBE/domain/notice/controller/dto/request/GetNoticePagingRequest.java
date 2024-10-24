package ddingdong.ddingdongBE.domain.notice.controller.dto.request;

import ddingdong.ddingdongBE.domain.notice.service.dto.command.GetNoticeListCommand;
import io.swagger.v3.oas.annotations.Parameter;
import org.springdoc.core.annotations.ParameterObject;

@ParameterObject
public record GetNoticePagingRequest(
    @Parameter(description = "현재 페이지 번호")
    int page,

    @Parameter(description = "현재 페이지 번호")
    Integer limit
) {

    public GetNoticeListCommand toCommand() {
        return GetNoticeListCommand.builder()
            .page(page)
            .limit((limit == null) ? 10 : limit)
            .build();
    }

}
