package ddingdong.ddingdongBE.domain.clubpost.controller.dto.response;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.file.controller.dto.response.FileUrlResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Builder
public record ClubPostListResponse(
    @Schema(description = "동아리명", example = "cow")
    String name,
    @Schema(description = "분과", example = "학술")
    String category,
    @Schema(description = "태그", example = "IT")
    String tag,
    @Schema(name = "동아리 프로필 이미지 정보", example = "https://%s.s3.%s.amazonaws.com/%s/%s/%s")
    FileUrlResponse profileImageFile,
    @Schema(name = "동아리 게시물 파일 정보리스트", example = "https://%s.s3.%s.amazonaws.com/%s/%s/%s")
    List<FileUrlResponse> clubPostFiles
) {

  public static ClubPostListResponse of(Club club, List<FileUrlResponse> clubPostFiles, FileUrlResponse profileImageFile) {
    return ClubPostListResponse.builder()
        .name(club.getName())
        .category(club.getCategory())
        .tag(club.getTag())
        .profileImageFile(profileImageFile)
        .clubPostFiles(clubPostFiles)
        .build();
  }
}
