package ddingdong.ddingdongBE.domain.documents.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import ddingdong.ddingdongBE.domain.documents.service.dto.query.DocumentListPagingQuery;
import ddingdong.ddingdongBE.domain.documents.service.dto.query.DocumentListPagingQuery.DocumentInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Schema(
    name = "DocumentListResponse",
    description = "자료실 자료 전체 조회 응답"
)
@Builder
public record DocumentListResponse(
        @Schema(description = "자료실 정보", implementation = DocumentDto.class)
        List<DocumentDto> documents,
        @Schema(description = "총 페이지 수", example = "10")
        Long totalPageCount
) {

    public static DocumentListResponse from(DocumentListPagingQuery query) {
        List<DocumentDto> documentDtos = query.documentInfos().stream()
            .map(DocumentDto::from)
            .toList();
        return DocumentListResponse.builder()
            .documents(documentDtos)
            .totalPageCount(query.totalPageCount())
            .build();
    }

    record DocumentDto(
        @Schema(description = "자료 식별자", example = "1")
        Long id,

        @Schema(description = "자료 제목", example = "자료 제목")
        String title,

        @Schema(description = "작성일", example = "2024-01-01")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate createdAt
    ) {

        public static DocumentDto from(DocumentInfo documentInfo) {
            return new DocumentDto(documentInfo.id(), documentInfo.title(), documentInfo.createdAt());
        }
    }
}
