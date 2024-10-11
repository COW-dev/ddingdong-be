package ddingdong.ddingdongBE.domain.filemetadata.dto.response;

import ddingdong.ddingdongBE.domain.filemetadata.service.dto.query.FileUrlQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.Builder;

@Schema(
    name = "FileUrlResponse",
    description = "파일 - 파일 Url 조회 응답"
)
@Builder
public record FileUrlResponse(
    @Schema(description = "조회 파일 식별자(UUID7)", example = "9b1deb4d-3b7d-4bad-9bdd-2b0d7b3dcb6d")
    UUID fileId,
    @Schema(description = "조회 파일 이름", example = "example.png")
    String fileName,
    @Schema(description = "fileUrl", example = "https://example.com")
    String fileUrl
) {

    public static FileUrlResponse of(FileUrlQuery query) {
        return FileUrlResponse.builder()
            .fileId(query.fileId())
            .fileName(query.fileName())
            .fileUrl(query.fileUrl())
            .build();
    }
}
