package ddingdong.ddingdongBE.domain.filemetadata.dto.request;

import com.github.f4b6a3.uuid.UuidCreator;
import ddingdong.ddingdongBE.domain.filemetadata.service.dto.command.FileMetaDataCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Schema(
    name = "FileMetaDataRequest",
    description = "파일 - 파일 정보 요청"
)
@Builder
public record FileMetaDataRequest(
    @Schema(description = "파일 id(UUID7)", example = "0191982e-1b5a-7069-9a47-7b4b2530d6a1")
    @NotNull(message = "fileId를 입력해주세요.")
    String fileId,

    @Schema(description = "파일 이름", example = "example.jpg")
    @NotNull(message = "fileName를 입력해주세요.")
    String fileName
) {

    public FileMetaDataCommand toCommand() {
        return FileMetaDataCommand.builder()
            .fileId(UuidCreator.fromString(fileId))
            .fileName(fileName)
            .build();
    }
}
