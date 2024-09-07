package ddingdong.ddingdongBE.domain.feed.controller.dto.response;

import ddingdong.ddingdongBE.domain.feed.service.dto.response.FeedInfo;
import ddingdong.ddingdongBE.domain.feed.vo.ClubInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record FeedResponse(
    @Schema(description = "피드 ID", example = "1")
    Long id,
    @Schema(description = "동아리 정보", implementation = ClubInfo.class)
    ClubInfo clubInfo,
    @Schema(description = "활동 내용", example = "안녕하세요. 카우 피드에요")
    String activityContent,
    @Schema(description = "CDN URL", example = "https://example.cloudfront.net")
    String fileUrl,
    @Schema(description = "피드 유형", example = "IMAGE")
    String feedType,
    @Schema(description = "생성 날짜", example = "2024-08-31")
    LocalDate createdDate
) {

  public static FeedResponse from(FeedInfo info) {
    return FeedResponse.builder()
        .id(info.id())
        .clubInfo(info.clubInfo())
        .activityContent(info.activityContent())
        .fileUrl(info.fileUrl())
        .feedType(info.feedType())
        .createdDate(info.createdDate())
        .build();
  }
}
