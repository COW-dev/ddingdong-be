package ddingdong.ddingdongBE.domain.feed.controller.dto.response;

import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedListQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedPageQuery;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

public record FeedPageResponse(
        @ArraySchema(schema = @Schema(name = "동아리 최신 피드 정보", implementation = FeedListResponse.class))
        List<FeedListResponse> newestFeeds,
        @Schema(name = "피드 페이지 정보", implementation = PagingResponse.class)
        PagingResponse pagingInfo
) {

    public static FeedPageResponse from(
            FeedPageQuery feedPageQuery) {
        List<FeedListResponse> newestFeeds = feedPageQuery.feedListQueries()
                .stream()
                .map(FeedListResponse::from)
                .toList();
        return new FeedPageResponse(newestFeeds,
                PagingResponse.from(feedPageQuery.pagingQuery()));
    }

    @Builder
    public record FeedListResponse(
            @Schema(description = "피드 ID", example = "1")
            Long id,
            @Schema(description = "피드 썸네일 CDN URL", example = "https://%s.s3.%s.amazonaws.com/%s/%s/%s")
            String thumbnailCdnUrl,
            @Schema(description = "피드 썸네일 S3 URL", example = "https://%s.s3.%s.amazonaws.com/%s/%s/%s")
            String thumbnailOriginUrl,
            @Schema(description = "피드 썸네일 파일 이름", example = "filename.jpg")
            String thumbnailFilename,
            @Schema(description = "피드 타입", example = "IMAGE")
            String feedType,
            @Schema(description = "조회수", example = "150")
            long viewCount,
            @Schema(description = "좋아요 수", example = "10")
            long likeCount,
            @Schema(description = "댓글 수", example = "5")
            long commentCount
    ) {

        public static FeedListResponse from(FeedListQuery query) {
            return FeedListResponse.builder()
                    .id(query.id())
                    .thumbnailOriginUrl(query.thumbnailOriginUrl())
                    .thumbnailCdnUrl(query.thumbnailCdnUrl())
                    .thumbnailFilename(query.thumbnailFileName())
                    .feedType(query.feedType())
                    .viewCount(query.viewCount())
                    .likeCount(query.likeCount())
                    .commentCount(query.commentCount())
                    .build();
        }
    }
}
