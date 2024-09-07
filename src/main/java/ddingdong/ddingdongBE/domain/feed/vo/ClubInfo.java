package ddingdong.ddingdongBE.domain.feed.vo;

import lombok.Builder;

@Builder
public record ClubInfo(
    String name,
    String profileImageUrl
) {

}
