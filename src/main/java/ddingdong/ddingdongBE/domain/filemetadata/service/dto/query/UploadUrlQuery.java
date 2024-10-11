package ddingdong.ddingdongBE.domain.filemetadata.service.dto.query;

import java.util.UUID;
import lombok.Builder;

@Builder
public record UploadUrlQuery(
    String uploadUrl,
    UUID fileId
) {

    public static UploadUrlQuery of(String uploadUrl, UUID fileId) {
        return UploadUrlQuery.builder()
            .uploadUrl(uploadUrl)
            .fileId(fileId)
            .build();
    }
}
