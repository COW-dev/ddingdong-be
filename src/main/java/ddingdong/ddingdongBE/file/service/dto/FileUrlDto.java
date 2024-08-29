package ddingdong.ddingdongBE.file.service.dto;

import ddingdong.ddingdongBE.file.entity.FileMetaData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record FileUrlDto(
        @Schema(description = "조회 파일 식별자(UUID7)", example = "9b1deb4d-3b7d-4bad-9bdd-2b0d7b3dcb6d")
        String fileId,
        @Schema(description = "조회 파일 이름", example = "example.png")
        String fileName
) {

    public static FileUrlDto from(FileMetaData fileMetaData) {
        return FileUrlDto.builder()
                .fileId(fileMetaData.getFileId().toString())
                .fileName(fileMetaData.getFileName())
                .build();
    }
}
