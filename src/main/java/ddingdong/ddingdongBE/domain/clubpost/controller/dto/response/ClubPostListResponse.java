package ddingdong.ddingdongBE.domain.clubpost.controller.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record ClubPostListResponse(
    List<String> fileUrls
) {

  public static ClubPostListResponse from(List<String> fileUrls) {
    return ClubPostListResponse.builder()
        .fileUrls(fileUrls)
        .build();
  }
}
