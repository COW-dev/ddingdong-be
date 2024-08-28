package ddingdong.ddingdongBE.file.controller.dto.request;

import ddingdong.ddingdongBE.file.service.dto.FileMetaDataCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(
        name = "UploadUrlResponse",
        description = "파일 - 업로드 url 조회 응답"
)
@Builder
public record FileMetaDataRequest(
        @Schema(description = "파일 id(UUID7)", example = "0191982e-1b5a-7069-9a47-7b4b2530d6a1")
        String fileId,
        @Schema(description = "파일 이름", example = "example.jpg")
        String fileName
) {

    public FileMetaDataCommand toCommand() {
        return FileMetaDataCommand.builder()
                .fileId(fileId)
                .fileName(fileName)
                .build();
    }

}
