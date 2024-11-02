package ddingdong.ddingdongBE.domain.documents.service.dto.query;

import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlAndNameQuery;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record DocumentQuery(
    String title,
    LocalDate createdAt,
    List<UploadedFileUrlAndNameQuery> fileInfoQueries
) {

    public static DocumentQuery of(Document document, List<UploadedFileUrlAndNameQuery> fileInfoQueries) {
        return DocumentQuery.builder()
            .title(document.getTitle())
            .createdAt(document.getCreatedAt().toLocalDate())
            .fileInfoQueries(fileInfoQueries)
            .build();
    }
}
