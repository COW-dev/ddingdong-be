package ddingdong.ddingdongBE.domain.fixzone.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.query.CentralFixZoneQuery;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.query.CentralFixZoneQuery.FixZoneCommentQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedImageUrlQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "상세 FixZone 응답")
public record CentralFixZoneResponse(
        @Schema(description = "픽스존 id")
        Long id,
        @Schema(description = "동아리 위치")
        String clubLocation,
        @Schema(description = "동아리명")
        String clubName,
        @Schema(description = "제목")
        String title,
        @Schema(description = "내용")
        String content,
        @Schema(description = "픽스존 완료 처리 여부")
        boolean isCompleted,
        @Schema(description = "요청 시각", pattern = "yyyy-MM-dd HH:mm:ss",  example = "2024-01-01 14:30:00")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime requestedAt,
        @Schema(description = "이미지 URL 목록")
        List<FixZoneImageUrlResponse> imageUrls,
        @Schema(description = "Fix Zone 댓글 목록")
        List<CentralFixZoneCommentResponse> comments
) {

    public static CentralFixZoneResponse from(CentralFixZoneQuery query) {
        return new CentralFixZoneResponse(
                query.id(),
                query.clubLocation(),
                query.clubName(),
                query.title(),
                query.content(),
                query.isCompleted(),
                query.requestedAt(),
                query.imageUrlQueries().stream()
                        .map(FixZoneImageUrlResponse::from)
                        .toList(),
                query.fixZoneCommentQueries().stream()
                        .map(CentralFixZoneCommentResponse::from)
                        .toList()
        );
    }

    @Schema(
            name = "FixZoneImageUrlResponse",
            description = "동아리 - 픽스존 이미지 URL 조회 응답"
    )
    record FixZoneImageUrlResponse(
            @Schema(description = "원본 url", example = "url")
            String originUrl,
            @Schema(description = "cdn url", example = "url")
            String cdnUrl
    ) {

        public static FixZoneImageUrlResponse from(UploadedImageUrlQuery query) {
            if (query == null) {
                return null;
            }
            return new FixZoneImageUrlResponse(query.originUrl(), query.cdnUrl());
        }

    }

    @Schema(name = "CentralFixZoneCommentResponse", description = "Fix Zone Comment 응답")
    public record CentralFixZoneCommentResponse(
            @Schema(description = "댓글 id")
            Long id,
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
            @Schema(description = "댓글 작성 시각", pattern = "yyyy-MM-dd HH:mm:ss", example = "2024-07-30 22:30:00")
            LocalDateTime createdAt,
            @Schema(description = "댓글 내용")
            String content,
            FixZoneCommentCommenterResponse commenter
    ) {

        public static CentralFixZoneCommentResponse from(FixZoneCommentQuery query) {
            return new CentralFixZoneCommentResponse(
                    query.id(),
                    query.createdAt(),
                    query.content(),
                    FixZoneCommentCommenterResponse.from(query)
            );
        }

        public record FixZoneCommentCommenterResponse(
                @Schema(description = "댓글 작성자")
                String name,
                FixZoneCommentCommenterProfileImageResponse profileImageUrl
        ) {

            public static FixZoneCommentCommenterResponse from(FixZoneCommentQuery query) {
                return new FixZoneCommentCommenterResponse(
                        query.commenter(),
                        FixZoneCommentCommenterProfileImageResponse.from(query.profileImageQuery())
                );
            }

            @Schema(
                    name = "FixZoneCommentCommenterProfileImageResponse",
                    description = "동아리 - 픽스존 이미지 URL 조회 응답"
            )
            record FixZoneCommentCommenterProfileImageResponse(
                    @Schema(description = "원본 url", example = "url")
                    String originUrl,
                    @Schema(description = "cdn url", example = "url")
                    String cdnUrl
            ) {

                public static FixZoneCommentCommenterProfileImageResponse from(
                    UploadedImageUrlQuery query) {
                    if (query == null) {
                        return null;
                    }
                    return new FixZoneCommentCommenterProfileImageResponse(query.originUrl(), query.cdnUrl());
                }

            }

        }

    }

}
