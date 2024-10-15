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
        @Schema(description = "업로드 key(경로)", example = "local/file/2024-01-01/cow/UUID")
        String key,
        @Schema(description = "contentType(presignedUrl 업로드 요청 시 사용)", example = "image/png")
        String contentType

) {

    public static UploadUrlResponse of(GeneratePreSignedUrlRequestQuery query, URL uploadUrl) {
        return UploadUrlResponse.builder()
                .uploadUrl(uploadUrl.toString())
                .key(query.key())
                .contentType(query.contentType())
                .build();
    }

}
