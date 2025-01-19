package ddingdong.ddingdongBE.file.service.dto;

import ddingdong.ddingdongBE.domain.fileinformation.entity.FileInformation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(
        name = "FileResponse",
        description = "파일 정보 응답"
)
@Getter
public class FileResponse {

    @Schema(description = "파일 이름", example = "a.pdf")
    private String name;

    @Schema(description = "파일 링크", example = "https://a.b")
    private String fileUrl;

    @Builder
    public FileResponse(String name, String fileUrl) {
        this.name = name;
        this.fileUrl = fileUrl;
    }

    public static FileResponse from(FileInformation fileInformation, String fileUrl) {
        return new FileResponse(fileInformation.getUploadName(), fileUrl);
    }
}
