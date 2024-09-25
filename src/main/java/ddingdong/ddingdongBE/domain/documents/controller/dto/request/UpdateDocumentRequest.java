package ddingdong.ddingdongBE.domain.documents.controller.dto.request;

import ddingdong.ddingdongBE.domain.documents.entity.Document;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(
        name = "ModifyDocumentRequest",
        description = "자료실 자료 수정 요청"
)
@Builder
public record UpdateDocumentRequest(
        @Schema(description = "자료 제목", example = "제목")
        String title
) {

    public Document toEntity() {
        return Document.builder()
                .title(title)
                .build();
    }
}
