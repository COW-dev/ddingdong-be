package ddingdong.ddingdongBE.domain.documents.service.dto.query;

import ddingdong.ddingdongBE.domain.documents.entity.Document;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record DocumentListQuery(
    Long id,
    String title,
    LocalDate createdAt
) {

    public static DocumentListQuery from(Document document) {
        return DocumentListQuery.builder()
            .id(document.getId())
            .title(document.getTitle())
            .createdAt(document.getCreatedAt().toLocalDate())
            .build();
    }
}
