package ddingdong.ddingdongBE.domain.documents.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import ddingdong.ddingdongBE.domain.documents.service.dto.query.DocumentQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Schema(
    name = "DocumentResponse",
    description = "자료실 자료 상세 조회 응답"
)
@Builder
public record DocumentResponse(
    @Schema(description = "자료 제목", example = "자료 제목")
    String title,

    @Schema(description = "작성일", example = "2024-01-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate createdAt,

    @ArraySchema(arraySchema = @Schema(description = "첨부파일 목록", implementation = DocumentFileUrlsResponse.class))
    List<DocumentFileUrlsResponse> fileUrls
) {

    public static DocumentResponse from(DocumentQuery query) {
        List<DocumentFileUrlsResponse> fileUrls = query.fileUrls().stream()
            .map(DocumentFileUrlsResponse::from)
            .toList();

        return DocumentResponse.builder()
            .title(query.title())
            .createdAt(query.createdAt())
            .fileUrls(fileUrls)
            .build();
    }

    @Schema(name = "DocumentFileUrlsResponse", description = "자료실 파일 URL 정보")
    record DocumentFileUrlsResponse(
        @Schema(description = "원본 URL", example = "url")
        String originUrl,
        @Schema(description = "CDN URL", example = "url")
        String cdnUrl
    ) {

        public static DocumentFileUrlsResponse from(UploadedFileUrlQuery query) {
            return new DocumentFileUrlsResponse(query.originUrl(), query.cdnUrl());
        }
    }
}
