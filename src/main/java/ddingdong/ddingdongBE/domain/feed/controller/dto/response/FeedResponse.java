package ddingdong.ddingdongBE.domain.feed.controller.dto.response;

import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedCommentQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubProfileQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedFileInfoQuery;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record FeedResponse(
        @Schema(description = "피드 ID", example = "1")
        Long id,
        @Schema(description = "활동 내용", example = "안녕하세요. 카우 피드에요")
        String activityContent,
        @Schema(description = "피드 유형", example = "IMAGE")
        String feedType,
        @Schema(description = "조회수", example = "150")
        long viewCount,
        @Schema(description = "좋아요 수", example = "10")
        long likeCount,
        @Schema(description = "댓글 수", example = "5")
        long commentCount,
        @Schema(description = "생성 날짜", example = "2024-08-31")
        LocalDate createdDate,
        @Schema(description = "URL 정보", implementation = FileUrlResponse.class)
        FileUrlResponse fileUrls,
        @Schema(description = "동아리 정보")
        ClubProfileResponse clubProfile,
        @ArraySchema(schema = @Schema(description = "댓글 목록", implementation = CommentResponse.class))
        List<CommentResponse> comments
) {

    @Builder
    record ClubProfileResponse(
            @Schema(description = "동아리 ID", example = "1")
            Long id,
            @Schema(description = "동아리 이름", example = "카우")
            String name,
            @Schema(description = "동아리 프로필 이미지 url", example = "https://%s.s3.%s.amazonaws.com/%s/%s/%s")
            String profileImageOriginUrl,
            @Schema(description = "동아리 프로필 이미지 url", example = "https://%s.s3.%s.amazonaws.com/%s/%s/%s")
            String profileImageCdnUrl,
            @Schema(description = "동아리 프로필 이미지 파일 이름", example = "filename.jpg")
            String profileImageFileName

    ) {

        public static ClubProfileResponse from(ClubProfileQuery query) {
            return ClubProfileResponse.builder()
                    .id(query.id())
                    .name(query.name())
                    .profileImageCdnUrl(query.profileImageCdnUrl())
                    .profileImageOriginUrl(query.profileImageOriginUrl())
                    .profileImageFileName(query.profileFileName())
                    .build();
        }
    }

    @Builder
    record FileUrlResponse(
            @Schema(description = "파일 식별자", example = "0192c828-ffce-7ee8-94a8-d9d4c8cdec00")
            String id,
            @Schema(description = "원본 url", example = "url")
            String originUrl,
            @Schema(description = "cdn url", example = "url")
            String cdnUrl,
            @Schema(description = "파일 이름", example = "filename.jpg")
            String fileName
    ) {

        public static FileUrlResponse from(FeedFileInfoQuery feedFileInfoQuery) {
            return FileUrlResponse.builder()
                    .id(feedFileInfoQuery.id())
                    .originUrl(feedFileInfoQuery.originUrl())
                    .cdnUrl(feedFileInfoQuery.cdnUrl())
                    .fileName(feedFileInfoQuery.fileName())
                    .build();
        }
    }

    @Builder
    public record CommentResponse(
            @Schema(description = "댓글 ID", example = "1")
            Long id,
            @Schema(description = "댓글 내용", example = "좋은 활동이네요!")
            String content,
            @Schema(description = "익명 이름", example = "익명1")
            String anonymousName,
            @Schema(description = "작성 일시")
            LocalDateTime createdAt
    ) {

        public static CommentResponse from(FeedCommentQuery query) {
            return CommentResponse.builder()
                    .id(query.id())
                    .content(query.content())
                    .anonymousName(query.anonymousName())
                    .createdAt(query.createdAt())
                    .build();
        }
    }

    public static FeedResponse from(FeedQuery query) {
        List<CommentResponse> commentResponses = query.comments() != null
                ? query.comments().stream().map(CommentResponse::from).toList()
                : List.of();
        return FeedResponse.builder()
                .id(query.id())
                .clubProfile(ClubProfileResponse.from(query.clubProfileQuery()))
                .activityContent(query.activityContent())
                .fileUrls(FileUrlResponse.from(query.feedFileInfoQuery()))
                .feedType(query.feedType())
                .viewCount(query.viewCount())
                .likeCount(query.likeCount())
                .commentCount(query.commentCount())
                .createdDate(query.createdDate())
                .comments(commentResponses)
                .build();
    }
}
