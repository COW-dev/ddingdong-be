package ddingdong.ddingdongBE.domain.filemetadata.dto.response;

import ddingdong.ddingdongBE.domain.filemetadata.service.dto.query.UploadUrlQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
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
    UUID fileId
) {

    public static UploadUrlResponse of(UploadUrlQuery query) {
        return UploadUrlResponse.builder()
            .uploadUrl(query.uploadUrl())
            .fileId(query.fileId())
            .build();
    }
}
