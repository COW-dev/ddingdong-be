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
        @Schema(description = "업로드 파일 식별자(UUID)", example = "9b1deb4d-3b7d-4bad-9bdd-2b0d7b3dcb6d")
        String fileId
) {

    public static UploadUrlResponse of(String uploadUrl, String fileId) {
        return UploadUrlResponse.builder()
                .uploadUrl(uploadUrl)
                .fileId(fileId)
                .build();
    }

}
