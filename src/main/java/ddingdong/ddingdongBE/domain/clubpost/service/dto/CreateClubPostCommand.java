package ddingdong.ddingdongBE.domain.clubpost.service.dto;

import ddingdong.ddingdongBE.domain.clubpost.entity.ClubPost;
import lombok.Builder;

@Builder
public record CreateClubPostCommand(
    Long userId,
    String title,
    String content,
    String mediaUrl
) {

  public ClubPost toEntity() {
    return ClubPost.create(title, content, mediaUrl);
  }
}
