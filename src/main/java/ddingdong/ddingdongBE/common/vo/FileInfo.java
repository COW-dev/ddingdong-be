package ddingdong.ddingdongBE.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;

public record FileInfo(
    @Schema(description = "파일 key", example = "{serverProfile}/{fileType}/2024-01-01/{authId}/{uuid}")
    String fileKey,

    @Schema(description = "파일 이름", example = "첨부파일1.pdf")
    String fileName
) {

}
