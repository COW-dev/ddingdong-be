package ddingdong.ddingdongBE.domain.feed.controller.dto.response;

import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.lang.NonNull;

@Builder
public record NewestFeedListResponse(
    @Schema(description = "피드 ID", example = "1")
    Long id,
    @Schema(description = "피드 썸네일 URL", example = "https://%s.s3.%s.amazonaws.com/%s/%s/%s")
    String thumbnailUrl,
    @Schema(description = "피드 타입", example = "IMAGE")
    String feedType
) {

  public static NewestFeedListResponse from(@NonNull Feed feed) {
    return NewestFeedListResponse.builder()
        .id(feed.getId())
        .thumbnailUrl(feed.getThumbnailUrl())
        .feedType(feed.getFeedType().toString())
        .build();
  }
}