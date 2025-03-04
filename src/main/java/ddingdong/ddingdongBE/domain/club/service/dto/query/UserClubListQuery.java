package ddingdong.ddingdongBE.domain.club.service.dto.query;

import ddingdong.ddingdongBE.domain.club.repository.dto.UserClubListInfo;

public record UserClubListQuery(
        Long id,
        String name,
        String category,
        String tag,
        String recruitStatus
) {

    public static UserClubListQuery of(UserClubListInfo userClubListInfo, String recruitStatus) {
        return new UserClubListQuery(
                userClubListInfo.getId(),
                userClubListInfo.getName(),
                userClubListInfo.getCategory(),
                userClubListInfo.getTag(),
                recruitStatus
        );
    }

}
