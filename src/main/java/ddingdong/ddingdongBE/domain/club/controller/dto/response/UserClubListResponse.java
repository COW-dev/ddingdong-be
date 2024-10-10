package ddingdong.ddingdongBE.domain.club.controller.dto.response;

import ddingdong.ddingdongBE.domain.club.service.dto.query.UserClubListQuery;

public record UserClubListResponse(
        Long id,
        String name,
        String category,
        String tag,
        String recruitStatus
) {

    public static UserClubListResponse from(UserClubListQuery query) {
        return new UserClubListResponse(
                query.id(),
                query.name(),
                query.category(),
                query.tag(),
                query.recruitStatus()
        );
    }

}
