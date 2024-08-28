package ddingdong.ddingdongBE.domain.clubpost.controller.dto.request;

import java.util.List;
import lombok.Builder;

@Builder
public record ClubFeedResponse(
    List<String> mediaUrl
) {

  public static ClubFeedResponse from(List<String> mediaUrl) {
    return ClubFeedResponse.builder()
        .mediaUrl(mediaUrl)
        .build();
  }
}
