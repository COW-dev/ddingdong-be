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
        String uploadUrl
) {

    public static UploadUrlResponse from(String uploadUrl) {
        return UploadUrlResponse.builder().uploadUrl(uploadUrl).build();
    }

}
