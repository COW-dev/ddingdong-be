package ddingdong.ddingdongBE.domain.feed.service.dto.query;

import lombok.Builder;

@Builder
public record ClubInfo(
    Long id,
    String name,
    String profileImageUrl
) {

}
