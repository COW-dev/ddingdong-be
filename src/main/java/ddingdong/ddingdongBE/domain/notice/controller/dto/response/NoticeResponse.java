package ddingdong.ddingdongBE.domain.notice.controller.dto.response;

import ddingdong.ddingdongBE.domain.notice.service.dto.query.NoticeQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedImageUrlQuery;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record NoticeResponse(
    @Schema(description = "공지사항 제목", example = "공지사항 제목입니다")
    String title,
    @Schema(description = "공지사항 내용", example = "카우 공지사항 내용입니다")
    String content,
    @Schema(description = "공지사항 생성 날짜 및 시간", example = "2024-01-01 12:12")
    LocalDateTime createdAt,
    @ArraySchema(arraySchema = @Schema(description = "이미지 목록", implementation = NoticeImageUrlsResponse.class))
    List<NoticeImageUrlsResponse> imageUrls,
    @ArraySchema(arraySchema = @Schema(description = "첨부파일 목록", implementation = NoticeFileUrlsResponse.class))
    List<NoticeFileUrlsResponse> fileUrls
) {

    public static NoticeResponse from(NoticeQuery query) {
        List<NoticeImageUrlsResponse> imageUrls = query.imageUrls().stream()
            .map(NoticeImageUrlsResponse::from)
            .toList();

        List<NoticeFileUrlsResponse> fileUrls = query.fileUrls().stream()
            .map(NoticeFileUrlsResponse::from)
            .toList();

        return NoticeResponse.builder()
            .title(query.title())
            .content(query.content())
            .createdAt(query.createdAt())
            .imageUrls(imageUrls)
            .fileUrls(fileUrls)
            .build();
    }

    @Schema(name = "NoticeImageUrlsResponse", description = "자료실 이미지 URL 목록")
    public record NoticeImageUrlsResponse(
        @Schema(description = "원본 URL", example = "url")
        String originUrl,
        @Schema(description = "CDN URL", example = "url")
        String cdnUrl
    ) {

        public static NoticeImageUrlsResponse from(UploadedImageUrlQuery query) {
            return new NoticeImageUrlsResponse(query.originUrl(), query.cdnUrl());
        }

    }

    @Schema(name = "NoticeFileUrlsResponse", description = "자료실 파일 URL 목록")
    public record NoticeFileUrlsResponse(
        @Schema(description = "원본 파일명", example = "보고서.pdf")
        String fileName,
        @Schema(description = "원본 URL", example = "url")
        String originUrl,
        @Schema(description = "CDN URL", example = "url")
        String cdnUrl
    ) {

        public static NoticeFileUrlsResponse from(UploadedFileUrlQuery query) {
            return new NoticeFileUrlsResponse(
                query.name(),
                query.originUrl(),
                query.cdnUrl()
            );
        }

    }

}
