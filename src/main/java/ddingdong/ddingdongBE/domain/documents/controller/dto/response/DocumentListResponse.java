package ddingdong.ddingdongBE.domain.documents.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import ddingdong.ddingdongBE.domain.documents.service.dto.query.DocumentListQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;

@Schema(
    name = "DocumentListResponse",
    description = "자료실 자료 전체 조회 응답"
)
@Builder
public record DocumentListResponse(
    @Schema(description = "자료 식별자", example = "1")
    Long id,

    @Schema(description = "자료 제목", example = "자료 제목")
    String title,

    @Schema(description = "작성일", example = "2024-01-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate createdAt
) {

    public static DocumentListResponse from(DocumentListQuery query) {
        return DocumentListResponse.builder()
            .id(query.id())
            .title(query.title())
            .createdAt(query.createdAt())
            .build();
    }
}
