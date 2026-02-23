package ddingdong.ddingdongBE.domain.feed.controller.dto.response;

import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedListQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.MyFeedPageQuery;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Builder
public record MyFeedPageResponse(
        @Schema(description = "총 피드 수", example = "15")
        long feedCount,
        @Schema(description = "총 조회수", example = "1200")
        long totalViewCount,
        @ArraySchema(schema = @Schema(name = "동아리 피드 정보", implementation = MyFeedListResponse.class))
        List<MyFeedListResponse> clubFeeds,
        @Schema(name = "피드 페이지 정보", implementation = PagingResponse.class)
        PagingResponse pagingInfo
) {

    public static MyFeedPageResponse from(MyFeedPageQuery myFeedPageQuery) {
        List<MyFeedListResponse> clubFeeds = myFeedPageQuery.feedListQueries().stream()
                .map(MyFeedListResponse::from)
                .toList();
        return MyFeedPageResponse.builder()
                .feedCount(myFeedPageQuery.feedCount())
                .totalViewCount(myFeedPageQuery.totalViewCount())
                .clubFeeds(clubFeeds)
                .pagingInfo(PagingResponse.from(myFeedPageQuery.pagingQuery()))
                .build();
    }

    @Builder
    record MyFeedListResponse(
            @Schema(description = "피드 ID", example = "1")
            Long id,
            @Schema(description = "피드 썸네일 CDN URL", example = "https://%s.s3.%s.amazonaws.com/%s/%s/%s")
            String thumbnailCdnUrl,
            @Schema(description = "피드 썸네일 S3 URL", example = "https://%s.s3.%s.amazonaws.com/%s/%s/%s")
            String thumbnailOriginUrl,
            @Schema(description = "피드 썸네일 파일 이름", example = "filename.jpg")
            String thumbnailFileName,
            @Schema(description = "피드 타입", example = "IMAGE")
            String feedType
    ) {

        public static MyFeedListResponse from(FeedListQuery feedListQuery) {
            return MyFeedListResponse.builder()
                    .id(feedListQuery.id())
                    .thumbnailCdnUrl(feedListQuery.thumbnailCdnUrl())
                    .thumbnailOriginUrl(feedListQuery.thumbnailOriginUrl())
                    .thumbnailFileName(feedListQuery.thumbnailFileName())
                    .feedType(feedListQuery.feedType())
                    .build();
        }
    }
}
