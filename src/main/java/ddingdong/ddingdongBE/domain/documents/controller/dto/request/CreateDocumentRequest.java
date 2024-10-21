package ddingdong.ddingdongBE.domain.documents.controller.dto.request;

import ddingdong.ddingdongBE.domain.documents.service.dto.command.CreateDocumentCommand;
import ddingdong.ddingdongBE.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Schema(
    name = "CreateDocumentRequest",
    description = "자료실 자료 생성 요청"
)
@Builder
public record CreateDocumentRequest(

    @Schema(description = "자료 제목", example = "자료 제목입니다")
    String title,

    @Schema(description = "자료 파일 Key", example = "[{serverProfile}/{contentType}/2024-01-01/{authId}/{uuid},"
        + " {serverProfile}/{contentType}/2024-01-02/{authId}/{uuid}]")
    List<String> keys
) {

    public CreateDocumentCommand toCommand(User admin) {
        return CreateDocumentCommand.builder()
            .title(title)
            .user(admin)
            .keys(keys)
            .build();
    }
}
