package ddingdong.ddingdongBE.domain.feed.controller.dto.response;

import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubFeedPageQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedListQuery;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

public record ClubFeedPageResponse(
    @ArraySchema(schema = @Schema(name = "동아리 피드 정보", implementation = ClubFeedListResponse.class))
    List<ClubFeedListResponse> clubFeeds,
    @Schema(name = "피드 페이지 정보", implementation = PagingResponse.class)
    PagingResponse pagingInfo
) {

    public static ClubFeedPageResponse from(ClubFeedPageQuery clubFeedPageQuery) {
        List<ClubFeedListResponse> clubFeeds = clubFeedPageQuery.feedListQueries().stream()
            .map(ClubFeedListResponse::from)
            .toList();
        return new ClubFeedPageResponse(clubFeeds, PagingResponse.from(clubFeedPageQuery.pagingQuery()));
    }

    @Builder
    record ClubFeedListResponse(
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

        public static ClubFeedListResponse from(FeedListQuery feedListQuery) {
            return ClubFeedListResponse.builder()
                .id(feedListQuery.id())
                .thumbnailCdnUrl(feedListQuery.thumbnailCdnUrl())
                .thumbnailOriginUrl(feedListQuery.thumbnailOriginUrl())
                .thumbnailFilename(feedListQuery.thumbnailFileName())
                .feedType(feedListQuery.feedType())
                .viewCount(feedListQuery.viewCount())
                .likeCount(feedListQuery.likeCount())
                .commentCount(feedListQuery.commentCount())
                .build();
        }
    }
}
