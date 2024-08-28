package ddingdong.ddingdongBE.domain.clubpost.controller.dto.request;

import ddingdong.ddingdongBE.domain.clubpost.service.dto.CreateClubPostCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Schema(
    name = "동아리 게시물 생성 요청"
)
public record CreateClubPostRequest(

    @Schema(description = "내용", example = "카우 일기장 내용입니다.")
    @Size(min = 2, max = 20, message = "게시물 활동 내용은 최소한 2글자 이상, 최대 20글자 이하이어야 합니다.")
    @NotNull(message = "게시물 내용은 필수로 입력해야 합니다.")
    String activityContent,
    @Schema(description = "이미지&동영상 url")
    @NotNull(message = "게시물 사진 혹은 동영상의 URL을 필수로 입력해야 합니다.")
    String mediaUrl
) {

  public CreateClubPostCommand toCommand(Long userId) {
    return CreateClubPostCommand.builder()
        .activityContent(activityContent)
        .mediaUrl(mediaUrl)
        .userId(userId)
        .build();
  }
}
