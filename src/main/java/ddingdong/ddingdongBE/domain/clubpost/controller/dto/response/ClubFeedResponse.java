package ddingdong.ddingdongBE.domain.clubpost.controller.dto.response;

import java.util.List;
import lombok.Builder;

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
