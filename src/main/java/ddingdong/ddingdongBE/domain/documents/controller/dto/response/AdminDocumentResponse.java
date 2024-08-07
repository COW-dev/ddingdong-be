package ddingdong.ddingdongBE.domain.documents.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import ddingdong.ddingdongBE.domain.documents.entity.Document;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;

@Schema(
        name = "AdminDocumentResponse",
        description = "어드민 자료실 자료 목록 조회 응답"
)
@Builder
public record AdminDocumentResponse(
        @Schema(description = "자료 식별자", example = "1")
        Long id,

        @Schema(description = "자료 제목", example = "자료 제목")
        String title,

        @Schema(description = "작성일", example = "2024-01-01")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate createdAt
) {

    public static AdminDocumentResponse from(Document document) {
        return AdminDocumentResponse.builder()
                .id(document.getId())
                .title(document.getTitle())
                .createdAt(document.getCreatedAt().toLocalDate())
                .build();
    }
}
