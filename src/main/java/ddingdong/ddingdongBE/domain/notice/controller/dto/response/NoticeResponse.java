package ddingdong.ddingdongBE.domain.notice.controller.dto.response;

import ddingdong.ddingdongBE.domain.notice.service.dto.query.NoticeQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlAndNameWithOrderQuery;
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

        @ArraySchema(schema = @Schema(description = "이미지 정보"))
        List<NoticeImageUrlResponse> images,

        @ArraySchema(schema = @Schema(description = "파일 정보"))
        List<NoticeFileUrlResponse> files
) {

    public static NoticeResponse from(NoticeQuery query) {
        return NoticeResponse.builder()
                .title(query.title())
                .content(query.content())
                .createdAt(query.createdAt())
                .images(query.images().stream()
                        .map(NoticeImageUrlResponse::from)
                        .toList())
                .files(query.files().stream()
                        .map(NoticeFileUrlResponse::from)
                        .toList())
                .build();
    }

    @Schema(
            name = "NoticeImageUrlResponse",
            description = "공지사항 이미지 정보 조회 응답"
    )
    public record NoticeImageUrlResponse(
            @Schema(description = "파일 식별자", example = "0192c828-ffce-7ee8-94a8-d9d4c8cdec00")
            String id,
            @Schema(description = "이미지 순서", example = "1")
            int order,
            @Schema(description = "원본 url", example = "url")
            String originUrl,
            @Schema(description = "cdn url", example = "url")
            String cdnUrl,
            @Schema(description = "파일 이름", example = "filename.jpg")
            String fileName
    ) {

        public static NoticeImageUrlResponse from(UploadedFileUrlAndNameWithOrderQuery query) {
            return new NoticeImageUrlResponse(query.id(), query.order(), query.originUrl(), query.cdnUrl(), query.fileName());
        }

    }

    @Schema(
            name = "NoticeFileUrlResponse",
            description = "공지사항 파일 정보 조회 응답"
    )
    public record NoticeFileUrlResponse(
            @Schema(description = "파일 식별자", example = "0192c828-ffce-7ee8-94a8-d9d4c8cdec00")
            String id,
            @Schema(description = "파일 순서", example = "1")
            int order,
            @Schema(description = "파일 이름", example = "파일명")
            String fileName,
            @Schema(description = "원본 url", example = "url")
            String originUrl,
            @Schema(description = "cdn url", example = "url")
            String cdnUrl
    ) {

        public static NoticeFileUrlResponse from(UploadedFileUrlAndNameWithOrderQuery query) {
            return new NoticeFileUrlResponse(
                    query.id(),
                    query.order(),
                    query.fileName(),
                    query.originUrl(),
                    query.cdnUrl()
            );
        }

    }

}
