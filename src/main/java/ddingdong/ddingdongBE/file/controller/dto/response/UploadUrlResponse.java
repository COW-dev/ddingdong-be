package ddingdong.ddingdongBE.file.controller.dto.response;

import ddingdong.ddingdongBE.file.service.dto.query.GeneratePreSignedUrlRequestQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.net.URL;
import lombok.Builder;

@Schema(
        name = "UploadUrlResponse",
        description = "파일 - 업로드 url 조회 응답"
)
@Builder
public record UploadUrlResponse(

        @Schema(description = "presignedUrl", example = "https://test-bucket.s3.amazonaws.com/test/jpg/image.jpg")
        String uploadUrl,
        @Schema(description = "파일 식별자", example = "0192c828-ffce-7ee8-94a8-d9d4c8cdec00")
        String id,
        @Schema(description = "contentType(presignedUrl 업로드 요청 시 사용)", example = "image/png")
        String contentType

) {

    public static UploadUrlResponse of(GeneratePreSignedUrlRequestQuery query, URL uploadUrl) {
        return UploadUrlResponse.builder()
                .uploadUrl(uploadUrl.toString())
                .id(query.id().toString())
                .contentType(query.contentType())
                .build();
    }

}
