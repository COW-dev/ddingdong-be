package ddingdong.ddingdongBE.file.controller.dto.response;

import ddingdong.ddingdongBE.file.entity.FileMetaData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(
        name = "FileUrlResponse",
        description = "파일 - 파일 Url 조회 응답"
)
@Builder
public record FileUrlResponse(
        @Schema(description = "조회 파일 식별자(UUID7)", example = "9b1deb4d-3b7d-4bad-9bdd-2b0d7b3dcb6d")
        String fileId,
        @Schema(description = "조회 파일 이름", example = "example.png")
        String fileName,
        @Schema(description = "fileUrl", example = "https://example.com")
        String fileUrl
) {

        public static FileUrlResponse of(FileMetaData fileMetaData, String fileUrl) {
                return FileUrlResponse.builder()
                        .fileId(fileMetaData.getFileId().toString())
                        .fileName(fileMetaData.getFileName())
                        .fileUrl(fileUrl)
                        .build();
        }
}
