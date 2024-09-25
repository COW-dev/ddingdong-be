package ddingdong.ddingdongBE.domain.documents.service.dto.query;

import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.file.dto.FileResponse;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record AdminDocumentQuery(
    String title,
    LocalDate createdAt,
    List<FileResponse> fileUrls
) {

    public static AdminDocumentQuery of(Document document, List<FileResponse> fileResponses) {
        return AdminDocumentQuery.builder()
            .title(document.getTitle())
            .createdAt(document.getCreatedAt().toLocalDate())
            .fileUrls(fileResponses)
            .build();
    }
}
