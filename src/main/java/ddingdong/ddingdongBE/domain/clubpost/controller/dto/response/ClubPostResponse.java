package ddingdong.ddingdongBE.domain.clubpost.controller.dto.response;

import ddingdong.ddingdongBE.domain.clubpost.entity.ClubPost;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record ClubPostResponse(
    String title,
    String content,
    String mediaUrl,
    String clubName,
    LocalDate crateDate
) {

  public static ClubPostResponse from(ClubPost clubPost) {
    return ClubPostResponse.builder()
        .title(clubPost.getTitle())
        .content(clubPost.getContent())
        .mediaUrl(clubPost.getMediaUrl())
        .clubName(clubPost.getClub().getName())
        .crateDate(LocalDate.from(clubPost.getCreatedAt()))
        .build();
  }
}
