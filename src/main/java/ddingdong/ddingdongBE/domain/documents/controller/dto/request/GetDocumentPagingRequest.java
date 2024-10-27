package ddingdong.ddingdongBE.domain.documents.controller.dto.request;

import ddingdong.ddingdongBE.common.constant.PageConstant;
import ddingdong.ddingdongBE.domain.documents.service.dto.command.GetDocumentListCommand;
import io.swagger.v3.oas.annotations.Parameter;
import org.springdoc.core.annotations.ParameterObject;

@ParameterObject
public record GetDocumentPagingRequest(
    @Parameter(description = "현재 페이지 번호")
    int page,

    @Parameter(description = "페이지당 항목 수")
    Integer limit
) {
    public GetDocumentListCommand toCommand() {
        return GetDocumentListCommand.builder()
            .page(page)
            .limit((limit == null) ? PageConstant.DEFAULT_DOCUMENT_PAGE_SIZE : limit)
            .build();
    }
}
