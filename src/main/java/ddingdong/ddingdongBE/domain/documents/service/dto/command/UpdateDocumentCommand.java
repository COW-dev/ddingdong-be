package ddingdong.ddingdongBE.domain.documents.service.dto.command;

import ddingdong.ddingdongBE.domain.documents.entity.Document;
import lombok.Builder;

@Builder
public record UpdateDocumentCommand(
    String title
) {

    public Document toEntity() {
        return Document.builder()
            .title(title)
            .build();
    }
}
