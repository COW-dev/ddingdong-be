package ddingdong.ddingdongBE.domain.feed.controller.dto.response;

import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedListQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.MyFeedPageQuery;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

public record MyFeedPageResponse(
    @ArraySchema(schema = @Schema(name = "동아리 피드 정보", implementation = MyFeedListResponse.class))
    List<MyFeedListResponse> clubFeeds,
    @Schema(name = "피드 페이지 정보", implementation = PagingResponse.class)
    PagingResponse pagingInfo
) {

    public static MyFeedPageResponse from(MyFeedPageQuery myFeedPageQuery) {
        List<MyFeedListResponse> clubFeeds = myFeedPageQuery.feedListQueries().stream()
            .map(MyFeedListResponse::from)
            .toList();
        return new MyFeedPageResponse(clubFeeds, PagingResponse.from(myFeedPageQuery.pagingQuery()));
    }

    @Builder
    record MyFeedListResponse(
        @Schema(description = "피드 ID", example = "1")
        Long id,
        @Schema(description = "피드 썸네일 CDN URL", example = "https://%s.s3.%s.amazonaws.com/%s/%s/%s")
        String thumbnailCdnUrl,
        @Schema(description = "피드 썸네일 S3 URL", example = "https://%s.s3.%s.amazonaws.com/%s/%s/%s")
        String thumbnailOriginUrl,
        @Schema(description = "피드 타입", example = "IMAGE")
        String feedType
    ) {

        public static MyFeedListResponse from(FeedListQuery feedListQuery) {
            return MyFeedListResponse.builder()
                .id(feedListQuery.id())
                .thumbnailCdnUrl(feedListQuery.thumbnailCdnUrl())
                .thumbnailOriginUrl(feedListQuery.thumbnailOriginUrl())
                .feedType(feedListQuery.feedType())
                .build();
        }
    }
}
