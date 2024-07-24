package ddingdong.ddingdongBE.domain.documents.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import ddingdong.ddingdongBE.domain.documents.entity.Document;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Schema(
        name = "DocumentResponse",
        description = "자료실 자료 목록 조회 응답"
)
@Getter
public class DocumentResponse {

    @Schema(description = "자료 식별자", example = "1")
    private final Long id;

    @Schema(description = "자료 제목", example = "자료 제목")
    private final String title;

    @Schema(description = "작성일", example = "2024-01-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate createdAt;

    @Builder
    private DocumentResponse(Long id, String title, LocalDate createdAt) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
    }

    public static DocumentResponse from(Document document) {
        return DocumentResponse.builder()
                .id(document.getId())
                .title(document.getTitle())
                .createdAt(document.getCreatedAt().toLocalDate())
                .build();
    }

}
