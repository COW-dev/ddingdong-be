package ddingdong.ddingdongBE.domain.club.service.dto.query;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedImageUrlQuery;
import java.time.LocalDateTime;

public record UserClubQuery(
        String name,
        String category,
        String tag,
        String leader,
        String phoneNumber,
        String location,
        LocalDateTime startRecruitPeriod,
        LocalDateTime endRecruitPeriod,
        String regularMeeting,
        String introduction,
        String activity,
        String ideal,
        String formUrl,
        UploadedImageUrlQuery profileImageUrlQuery,
        UploadedImageUrlQuery introductionImageUrlQuery
) {

    public static UserClubQuery of(
            Club club,
        UploadedImageUrlQuery profileImageUrlQuery,
        UploadedImageUrlQuery introductionImageUrlQuery) {
        return new UserClubQuery(
                club.getName(),
                club.getCategory(),
                club.getTag(),
                club.getLeader(),
                club.getPhoneNumber().getNumber(),
                club.getLocation().getValue(),
                club.getStartRecruitPeriod(),
                club.getEndRecruitPeriod(),
                club.getRegularMeeting(),
                club.getIntroduction(),
                club.getActivity(),
                club.getIdeal(),
                club.getFormUrl(),
                profileImageUrlQuery,
                introductionImageUrlQuery
        );
    }

}
