package ddingdong.ddingdongBE.domain.feed.service.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record ClubInfo(
    @Schema(description = "동아리 이름", example = "카우")
    String name,
    @Schema(description = "동아리 프로필 이미지 url", example = "https://%s.s3.%s.amazonaws.com/%s/%s/%s")
    String profileImageUrl
) {

}
