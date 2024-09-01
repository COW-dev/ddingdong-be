package ddingdong.ddingdongBE.domain.documents.controller.dto.request;

import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(
        name = "GenerateDocumentRequest",
        description = "자료실 자료 생성 요청"
)
@Builder
public record GenerateDocumentRequest(
        @Schema(description = "자료 제목", example = "제목")
        String title
) {
    public Document toEntity(User user) {
        return Document.builder()
                .user(user)
                .title(title)
                .build();
    }

}
