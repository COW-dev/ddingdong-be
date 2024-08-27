package ddingdong.ddingdongBE.domain.clubpost.service;

import ddingdong.ddingdongBE.domain.clubpost.entity.ClubPost;
import lombok.Builder;

@Builder
public record UpdateClubPostCommand(
    Long clubPostId,
    String title,
    String content,
    String mediaUrl
) {

  public ClubPost toEntity() {
    return ClubPost.builder()
        .title(title)
        .content(content)
        .mediaUrl(mediaUrl)
        .build();
  }
}
