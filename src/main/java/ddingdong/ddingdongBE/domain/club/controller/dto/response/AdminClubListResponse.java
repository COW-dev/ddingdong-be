package ddingdong.ddingdongBE.domain.club.controller.dto.response;

import ddingdong.ddingdongBE.domain.club.service.dto.query.AdminClubListQuery;
import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;

@Builder
public record AdminClubListResponse(
        Long id,
        String name,
        String category,
        BigDecimal score,
        List<String> profileImageUrls
) {

    public static AdminClubListResponse from(AdminClubListQuery query) {
        return AdminClubListResponse.builder()
                .id(query.id())
                .name(query.name())
                .category(query.category())
                .score(query.score())
                .profileImageUrls(query.profileImageUrls()).build();
    }

}
