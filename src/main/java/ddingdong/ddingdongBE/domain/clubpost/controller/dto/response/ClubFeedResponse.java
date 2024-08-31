package ddingdong.ddingdongBE.domain.clubpost.controller.dto.response;

import ddingdong.ddingdongBE.file.controller.dto.response.FileUrlResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Schema(
    name = "ClubFeedResponse",
    description = "동아리 피드 전체 조회"
)
@Builder
public record ClubFeedResponse(
    @Schema(name = "동아리 게시물 파일 정보리스트", example = "https://%s.s3.%s.amazonaws.com/%s/%s/%s")
    List<FileUrlResponse> clubPostFiles
) {

  public static ClubFeedResponse from(List<FileUrlResponse> clubPostFiles) {
    return ClubFeedResponse.builder()
        .clubPostFiles(clubPostFiles)
        .build();
  }
}
