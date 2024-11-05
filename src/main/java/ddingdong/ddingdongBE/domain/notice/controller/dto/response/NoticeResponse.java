package ddingdong.ddingdongBE.domain.notice.controller.dto.response;

import ddingdong.ddingdongBE.domain.notice.service.dto.query.NoticeQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlAndNameQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
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

    @ArraySchema(schema = @Schema(description = "이미지 Urls", example = "https://%s.s3.%s.amazonaws.com/%s/%s/%s"))
    List<UploadedFileUrlQuery> images,

    @ArraySchema(schema = @Schema(description = "파일 상세 정보"))
    List<UploadedFileUrlAndNameQuery> files
) {

    public static NoticeResponse from(NoticeQuery query) {
        return NoticeResponse.builder()
            .title(query.title())
            .content(query.content())
            .createdAt(query.createdAt())
            .images(query.images())
            .files(query.files())
            .build();
    }

}
