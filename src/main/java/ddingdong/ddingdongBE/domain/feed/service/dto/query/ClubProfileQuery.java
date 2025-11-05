package ddingdong.ddingdongBE.domain.feed.service.dto.query;

import lombok.Builder;

@Builder
public record ClubProfileQuery(
    Long id,
    String name,
    String profileImageOriginUrl,
    String profileImageCdnUrl
) {

}
