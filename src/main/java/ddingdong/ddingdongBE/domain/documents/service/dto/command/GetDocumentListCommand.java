package ddingdong.ddingdongBE.domain.documents.service.dto.command;

import lombok.Builder;

@Builder
public record GetDocumentListCommand(
    int page,
    int limit
) {

}
