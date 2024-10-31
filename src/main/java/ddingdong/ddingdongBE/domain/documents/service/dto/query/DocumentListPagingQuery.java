package ddingdong.ddingdongBE.domain.documents.service.dto.query;

import ddingdong.ddingdongBE.domain.documents.entity.Document;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record DocumentListPagingQuery(
    List<DocumentInfo> documentInfos,
    int totalPageCount
) {

    public static DocumentListPagingQuery of(List<Document> documents, int totalPageCount) {
        List<DocumentInfo> documentInfos = documents.stream()
                .map(DocumentInfo::from)
                .toList();
        return DocumentListPagingQuery.builder()
            .documentInfos(documentInfos)
            .totalPageCount(totalPageCount)
            .build();
    }

    public record DocumentInfo(
            Long id,
            String title,
            LocalDate createdAt
    ) {

        public static DocumentInfo from(Document document) {
            return new DocumentInfo(document.getId(), document.getTitle(), document.getCreatedAt().toLocalDate());
        }

    }
}
