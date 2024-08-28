package ddingdong.ddingdongBE.domain.clubpost.controller.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record ClubPostListResponse(
    List<String> mediaUrl
) {

  public static ClubPostListResponse from(List<String> mediaUrl) {
    return ClubPostListResponse.builder()
        .mediaUrl(mediaUrl)
        .build();
  }
}
