package ddingdong.ddingdongBE.domain.clubpost.controller.dto.request;

import ddingdong.ddingdongBE.domain.clubpost.service.dto.UpdateClubPostCommand;
import ddingdong.ddingdongBE.file.controller.dto.request.FileMetaDataRequest;
import ddingdong.ddingdongBE.file.service.dto.FileMetaDataCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Schema(
    name = "동아리 게시물 수정 요청"
)
public record UpdateClubPostRequest(
    @Schema(description = "활동 내용", example = "카우 일기장 내용입니다.")
    @Size(min = 2, max = 20, message = "게시물 활동 내용은 최소한 2글자 이상, 최대 20글자 이하이어야 합니다.")
    @NotNull(message = "게시물 내용은 필수로 입력해야 합니다.")
    String activityContent,

    @Schema(description = "이미지&동영상 id와 name")
    @NotNull(message = "게시물 사진 혹은 동영상의 id와 name을 필수로 입력해야 합니다.")
    FileMetaDataRequest fileMetaDataRequest
) {

  public UpdateClubPostCommand toCommand(Long clubPostId) {
    return UpdateClubPostCommand.builder()
        .activityContent(activityContent)
        .fileMetaDataCommand(getClubPostInfoCommand())
        .clubPostId(clubPostId)
        .build();
  }

  private FileMetaDataCommand getClubPostInfoCommand() {
    return Optional.ofNullable(fileMetaDataRequest)
        .map(FileMetaDataRequest::toCommand)
        .orElse(null);
  }
}
