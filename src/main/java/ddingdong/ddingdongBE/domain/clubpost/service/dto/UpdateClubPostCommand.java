package ddingdong.ddingdongBE.domain.clubpost.service.dto;

import ddingdong.ddingdongBE.domain.clubpost.entity.ClubPost;
import ddingdong.ddingdongBE.file.service.dto.FileMetaDataCommand;
import lombok.Builder;

@Builder
public record UpdateClubPostCommand(
    Long clubPostId,
    String activityContent,
    FileMetaDataCommand fileMetaDataCommand
) {

  public ClubPost toEntity(String fileUrl) {
    return ClubPost.builder()
        .activityContent(activityContent)
        .fileUrl(fileUrl)
        .build();
  }
}
