package ddingdong.ddingdongBE.domain.documents.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import ddingdong.ddingdongBE.domain.documents.entity.Document;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record GetDocumentByPageResponse(
    List<DocumentDto> documents
) {

    public static GetDocumentByPageResponse from(List<Document> documents) {
        return GetDocumentByPageResponse.builder()
            .documents(
                documents.stream()
                    .map(DocumentDto::from)
                    .toList()
            )
            .build();
    }

    @Builder
    public record DocumentDto(
        @Schema(description = "자료 식별자", example = "1")
        Long id,

        @Schema(description = "자료 제목", example = "자료 제목")
        String title,

        @Schema(description = "작성일", example = "2024-01-01")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate createdAt
    ) {

        public static DocumentDto from(Document document) {
            return DocumentDto.builder()
                .id(document.getId())
                .title(document.getTitle())
                .createdAt(document.getCreatedAt().toLocalDate())
                .build();
        }

    }
}
