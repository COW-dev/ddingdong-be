package ddingdong.ddingdongBE.domain.clubpost.controller.dto.response;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.clubpost.entity.ClubPost;
import java.util.List;
import lombok.Builder;

@Builder
public record ClubPostListResponse(
    String clubName,
    String clubProfileImageUrl,
    String category,
    String tag,
    List<String> fileUrls
) {

  public static ClubPostListResponse of(List<String> fileUrls, Club club) {
    return ClubPostListResponse.builder()
        .clubName(club.getName())
        .clubProfileImageUrl(club.getProfileImageUrl())
        .category()
        .tag()
        .fileUrls(fileUrls)
        .build();
  }
}
