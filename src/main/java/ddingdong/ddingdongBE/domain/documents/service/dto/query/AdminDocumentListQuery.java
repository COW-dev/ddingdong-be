package ddingdong.ddingdongBE.domain.documents.service.dto.query;

import ddingdong.ddingdongBE.domain.documents.entity.Document;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record AdminDocumentListQuery(
    Long id,
    String title,
    LocalDate createdAt
) {

    public static AdminDocumentListQuery from(Document document) {
        return AdminDocumentListQuery.builder()
            .id(document.getId())
            .title(document.getTitle())
            .createdAt(document.getCreatedAt().toLocalDate())
            .build();
    }
}
