package ddingdong.ddingdongBE.domain.documents.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.file.dto.FileResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Schema(
        name = "DetailDocumentResponse",
        description = "자료실 자료 상세 조회 응답"
)
@Getter
public class DetailDocumentResponse {

    @Schema(description = "자료 제목", example = "자료 제목")
    private final String title;

    @Schema(description = "자료 내용", example = "자료 내용")
    private final String content;

    @Schema(description = "작성일", example = "2024-01-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate createdAt;

    @ArraySchema(schema = @Schema(description = "첨부파일 목록", implementation = FileResponse.class))
    private final List<FileResponse> fileUrls;

    @Builder
    private DetailDocumentResponse(String title, String content, LocalDate createdAt,
                                   List<FileResponse> fileUrls) {
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.fileUrls = fileUrls;
    }

    public static DetailDocumentResponse of(Document document,
                                            List<FileResponse> fileResponses) {
        return DetailDocumentResponse.builder()
                .title(document.getTitle())
                .content(document.getContent())
                .createdAt(document.getCreatedAt().toLocalDate())
                .fileUrls(fileResponses)
                .build();
    }
}
