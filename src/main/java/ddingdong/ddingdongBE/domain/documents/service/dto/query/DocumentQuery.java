package ddingdong.ddingdongBE.domain.documents.service.dto.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.file.dto.FileResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record DocumentQuery(
    @Schema(description = "자료 제목", example = "자료 제목")
    String title,

    @Schema(description = "작성일", example = "2024-01-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate createdAt,

    @ArraySchema(schema = @Schema(description = "첨부파일 목록", implementation = FileResponse.class))
    List<FileResponse> fileUrls
) {

    public static DocumentQuery of(Document document, List<FileResponse> fileUrls) {
        return DocumentQuery.builder()
            .title(document.getTitle())
            .createdAt(document.getCreatedAt().toLocalDate())
            .fileUrls(fileUrls)
            .build();
    }
}
