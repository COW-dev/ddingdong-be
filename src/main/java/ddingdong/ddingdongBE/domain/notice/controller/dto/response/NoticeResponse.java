package ddingdong.ddingdongBE.domain.notice.controller.dto.response;

import ddingdong.ddingdongBE.domain.notice.service.dto.query.NoticeQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;
import lombok.Builder;

@Builder
public record NoticeResponse(
    @Schema(description = "공지사항 제목", example = "공지사항 제목입니다")
    String title,

    @Schema(description = "공지사항 내용", example = "카우 공지사항 내용입니다")
    String content,

    @Schema(description = "공지사항 생성 날짜 및 시간", example = "2024-01-01 12:12")
    LocalDateTime createdAt,

    @ArraySchema(schema = @Schema(description = "이미지 Urls", example = "https://%s.s3.%s.amazonaws.com/%s/%s/%s"))
    List<UploadedFileUrlQuery> imageUrls,

    @ArraySchema(schema = @Schema(description = "파일 상세 정보", implementation = FileDetail.class))
    List<FileDetail> fileInfos
) {

    public static NoticeResponse from(NoticeQuery query) {
        List<FileDetail> fileDetails = IntStream.range(0, query.fileNames().size())
            .mapToObj(i -> FileDetail.from(query, i))
            .toList();

        return NoticeResponse.builder()
            .title(query.title())
            .content(query.content())
            .createdAt(query.createdAt())
            .imageUrls(query.imageUrls())
            .fileInfos(fileDetails)
            .build();
    }

    @Builder
    public record FileDetail(
        @Schema(description = "파일 이름", example = "첨부파일1.pdf")
        String fileName,

        @Schema(description = "원본 파일 URL", example = "https://example-origin.com/file.pdf")
        String originUrl,

        @Schema(description = "CDN 파일 URL", example = "https://example-cdn.com/file.pdf")
        String cdnUrl
    ) {

        public static FileDetail from(NoticeQuery query, int index) {
            return FileDetail.builder()
                .fileName(query.fileNames().get(index))
                .originUrl(query.fileUrls().get(index).originUrl())
                .cdnUrl(query.fileUrls().get(index).cdnUrl())
                .build();
        }
    }

}
