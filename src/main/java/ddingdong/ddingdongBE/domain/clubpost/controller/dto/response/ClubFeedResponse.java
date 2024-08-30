package ddingdong.ddingdongBE.domain.clubpost.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Schema(
    name = "ClubFeedResponse",
    description = "동아리 피드 전체 조회"
)
@Builder
public record ClubFeedResponse(
    List<String> mediaUrls
) {

  public static ClubFeedResponse from(List<String> mediaUrls) {
    return ClubFeedResponse.builder()
        .mediaUrls(mediaUrls)
        .build();
  }
}
