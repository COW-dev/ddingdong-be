package ddingdong.ddingdongBE.domain.clubpost.controller.dto.response;

import ddingdong.ddingdongBE.domain.clubpost.entity.ClubPost;
import ddingdong.ddingdongBE.file.controller.dto.response.FileUrlResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record ClubPostResponse(
    @Schema(name = "활동 내용", example = "카우 활동내역입니다.")
    String activityContent,
    @Schema(name = "동아리 명", example = "카우")
    String name,
    @Schema(name = "동아리 게시물 파일 정보", example = "https://%s.s3.%s.amazonaws.com/%s/%s/%s")
    FileUrlResponse clubPostFile,
    @Schema(name = "동아리 프로필 이미지 정보", example = "https://%s.s3.%s.amazonaws.com/%s/%s/%s")
    FileUrlResponse profileImageFile,
    @Schema(name = "생성 날짜", example = "2024-xx-xx")
    LocalDate createdDate
) {

  public static ClubPostResponse of(ClubPost clubPost, FileUrlResponse postFileResponse, FileUrlResponse clubProfileImageResponse) {
    return ClubPostResponse.builder()
        .activityContent(clubPost.getActivityContent())
        .name(clubPost.getClub().getName())
        .clubPostFile(postFileResponse)
        .profileImageFile(clubProfileImageResponse)
        .createdDate(LocalDate.from(clubPost.getCreatedAt()))
        .build();
  }
}
