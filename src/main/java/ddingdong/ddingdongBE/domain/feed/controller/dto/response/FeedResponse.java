package ddingdong.ddingdongBE.domain.feed.controller.dto.response;

import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubInformationQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record FeedResponse(
    @Schema(description = "피드 ID", example = "1")
    Long id,
    @Schema(description = "동아리 정보")
    ClubInformationResponse clubInfo,
    @Schema(description = "활동 내용", example = "안녕하세요. 카우 피드에요")
    String activityContent,
    @Schema(description = "CDN URL", example = "https://example.cloudfront.net")
    String fileUrl,
    @Schema(description = "피드 유형", example = "IMAGE")
    String feedType,
    @Schema(description = "생성 날짜", example = "2024-08-31")
    LocalDate createdDate
) {

  @Builder
  record ClubInformationResponse(
      @Schema(description = "동아리 ID", example = "1")
      Long id,
      @Schema(description = "동아리 이름", example = "카우")
      String name,
      @Schema(description = "동아리 프로필 이미지 url", example = "https://%s.s3.%s.amazonaws.com/%s/%s/%s")
      String profileImageUrl
  ) {
    public static ClubInformationResponse from(ClubInformationQuery query) {
      return ClubInformationResponse.builder()
          .id(query.id())
          .name(query.name())
          .profileImageUrl(query.profileImageUrl())
          .build();
    }
  }

  public static FeedResponse from(FeedQuery info) {
    return FeedResponse.builder()
        .id(info.id())
        .clubInfo(ClubInformationResponse.from(info.clubInformationQuery()))
        .activityContent(info.activityContent())
        .fileUrl(info.fileUrl())
        .feedType(info.feedType())
        .createdDate(info.createdDate())
        .build();
  }
}
