package ddingdong.ddingdongBE.domain.clubpost.service.dto;

import ddingdong.ddingdongBE.domain.clubpost.entity.ClubPost;
import lombok.Builder;

@Builder
public record UpdateClubPostCommand(
    Long clubPostId,
    String activityContent,
    String mediaUrl
) {

  public ClubPost toEntity() {
    return ClubPost.builder()
        .activityContent(activityContent)
        .mediaUrl(mediaUrl)
        .build();
  }
}
