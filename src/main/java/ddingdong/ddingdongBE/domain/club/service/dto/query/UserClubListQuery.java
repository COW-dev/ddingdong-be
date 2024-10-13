package ddingdong.ddingdongBE.domain.club.service.dto.query;

import ddingdong.ddingdongBE.domain.club.entity.Club;

public record UserClubListQuery(
        Long id,
        String name,
        String category,
        String tag,
        String recruitStatus
) {

    public static UserClubListQuery of(Club club, String recruitStatus) {
        return new UserClubListQuery(
                club.getId(),
                club.getName(),
                club.getCategory(),
                club.getTag(),
                recruitStatus
        );
    }

}
