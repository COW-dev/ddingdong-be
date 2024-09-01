package ddingdong.ddingdongBE.domain.documents.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.file.dto.FileResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Schema(
        name = "AdminDetailDocumentResponse",
        description = "어드민 자료실 자료 상세 조회 응답"
)
@Builder
public record AdminDetailDocumentResponse(
        @Schema(description = "자료 제목", example = "자료 제목")
        String title,

        @Schema(description = "작성일", example = "2024-01-01")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate createdAt,

        @ArraySchema(schema = @Schema(description = "첨부파일 목록", implementation = FileResponse.class))
        List<FileResponse> fileUrls
) {

    public static AdminDetailDocumentResponse of(Document document,
                                                 List<FileResponse> fileResponses) {
        return AdminDetailDocumentResponse.builder()
                .title(document.getTitle())
                .createdAt(document.getCreatedAt().toLocalDate())
                .fileUrls(fileResponses)
                .build();
    }
}
