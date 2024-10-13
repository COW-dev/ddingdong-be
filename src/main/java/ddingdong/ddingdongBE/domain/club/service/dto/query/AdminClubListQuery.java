package ddingdong.ddingdongBE.domain.club.service.dto.query;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;

@Builder
public record AdminClubListQuery(
        Long id,
        String name,
        String category,
        BigDecimal score,
        List<String> profileImageUrls
) {

    public static AdminClubListQuery of(Club club, List<String> profileImageUrls) {
        return AdminClubListQuery.builder()
                .id(club.getId())
                .name(club.getName())
                .category(club.getCategory())
                .score(club.getScore().getValue())
                .profileImageUrls(profileImageUrls).build();
    }

}
