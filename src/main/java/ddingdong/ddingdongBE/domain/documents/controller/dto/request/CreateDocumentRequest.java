package ddingdong.ddingdongBE.domain.documents.controller.dto.request;

import ddingdong.ddingdongBE.domain.documents.service.dto.command.CreateDocumentCommand;
import ddingdong.ddingdongBE.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Schema(
    name = "CreateDocumentRequest",
    description = "자료실 자료 생성 요청"
)
@Builder
public record CreateDocumentRequest(
    @Schema(description = "자료 제목", example = "자료 제목입니다")
    String title
) {

    public CreateDocumentCommand toCommand(User admin, List<MultipartFile> uploadFiles) {
        return CreateDocumentCommand.builder()
            .title(title)
            .user(admin)
            .uploadFiles(uploadFiles)
            .build();
    }
}
