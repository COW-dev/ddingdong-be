package ddingdong.ddingdongBE.domain.feed.controller.dto.response;

import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedListQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record NewestFeedListResponse(
    @Schema(description = "피드 ID", example = "1")
    Long id,
    @Schema(description = "피드 썸네일 URL", example = "https://%s.s3.%s.amazonaws.com/%s/%s/%s")
    String thumbnailUrl,
    @Schema(description = "피드 타입", example = "IMAGE")
    String feedType
) {

  public static NewestFeedListResponse from(FeedListQuery info) {
    return NewestFeedListResponse.builder()
        .id(info.id())
        .thumbnailUrl(info.thumbnailUrl())
        .feedType(info.feedType())
        .build();
  }
}
