package ddingdong.ddingdongBE.domain.feed.service.dto.query;

import lombok.Builder;

@Builder
public record ClubInformationQuery(
    Long id,
    String name,
    String profileImageUrl
) {

}
