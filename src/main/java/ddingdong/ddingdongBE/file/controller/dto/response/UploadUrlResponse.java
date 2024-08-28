package ddingdong.ddingdongBE.file.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(
        name = "UploadUrlResponse",
        description = "파일 - 업로드 url 조회 응답"
)
@Builder
public record UploadUrlResponse(

        @Schema(description = "presignedUrl", example = "https://test-bucket.s3.amazonaws.com/test/jpg/image.jpg")
        String uploadUrl,
        @Schema(description = "업로드 파일 이름(UUID)", example = "9b1deb4d-3b7d-4bad-9bdd-2b0d7b3dcb6d")
        String uploadFileName
) {

    public static UploadUrlResponse of(String uploadUrl, String uploadFileName) {
        return UploadUrlResponse.builder()
                .uploadUrl(uploadUrl)
                .uploadFileName(uploadFileName)
                .build();
    }

}
