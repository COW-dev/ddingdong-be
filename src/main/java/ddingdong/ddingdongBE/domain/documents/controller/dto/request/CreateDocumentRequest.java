package ddingdong.ddingdongBE.domain.documents.controller.dto.request;

import ddingdong.ddingdongBE.common.vo.FileInfo;
import ddingdong.ddingdongBE.domain.documents.service.dto.command.CreateDocumentCommand;
import ddingdong.ddingdongBE.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;

@Schema(
    name = "CreateDocumentRequest",
    description = "자료실 자료 생성 요청"
)
@Builder
public record CreateDocumentRequest(

    @NotNull(message = "자료 제목은 필수입니다.")
    @Schema(description = "자료 제목", example = "자료 제목입니다")
    String title,

    @NotNull(message = "자료 파일 정보는 필수입니다")
    @Schema(description = "자료 정보", implementation = FileInfo.class)
    List<FileInfo> fileInfos
) {

    public CreateDocumentCommand toCommand(User admin) {
        return CreateDocumentCommand.builder()
            .title(title)
            .user(admin)
            .fileInfos(fileInfos)
            .build();
    }
}
