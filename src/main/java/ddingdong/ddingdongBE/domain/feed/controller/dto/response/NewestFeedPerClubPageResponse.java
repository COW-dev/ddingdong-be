package ddingdong.ddingdongBE.domain.feed.controller.dto.response;

import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedListQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.NewestFeedPerClubPageQuery;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

public record NewestFeedPerClubPageResponse(
    @ArraySchema(schema = @Schema(name = "동아리 최신 피드 정보", implementation = NewestFeedListResponse.class))
    List<NewestFeedListResponse> newestFeeds,
    @Schema(name = "피드 페이지 정보", implementation = PagingResponse.class)
    PagingResponse pagingInfo
) {

    public static NewestFeedPerClubPageResponse from(NewestFeedPerClubPageQuery newestFeedPerClubPageQuery) {
        List<NewestFeedListResponse> newestFeeds = newestFeedPerClubPageQuery.feedListQueries().stream()
            .map(NewestFeedListResponse::from)
            .toList();
        return new NewestFeedPerClubPageResponse(newestFeeds,
            PagingResponse.from(newestFeedPerClubPageQuery.pagingQuery()));
    }

    @Builder
    public record NewestFeedListResponse(
        @Schema(description = "피드 ID", example = "1")
        Long id,
        @Schema(description = "피드 썸네일 CDN URL", example = "https://%s.s3.%s.amazonaws.com/%s/%s/%s")
        String thumbnailCdnUrl,
        @Schema(description = "피드 썸네일 S3 URL", example = "https://%s.s3.%s.amazonaws.com/%s/%s/%s")
        String thumbnailOriginUrl,
        @Schema(description = "피드 타입", example = "IMAGE")
        String feedType
    ) {

        public static NewestFeedListResponse from(FeedListQuery query) {
            return NewestFeedListResponse.builder()
                .id(query.id())
                .thumbnailOriginUrl(query.thumbnailOriginUrl())
                .thumbnailCdnUrl(query.thumbnailCdnUrl())
                .feedType(query.feedType())
                .build();
        }
    }
}
