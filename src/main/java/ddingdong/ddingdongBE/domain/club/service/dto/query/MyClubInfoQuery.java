package ddingdong.ddingdongBE.domain.club.service.dto.query;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import java.time.LocalDateTime;

public record MyClubInfoQuery(
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
        UploadedFileUrlQuery profileImageUrlQuery,
        UploadedFileUrlQuery introductionImageUrlQuery
) {

    public static MyClubInfoQuery of(
            Club club,
            UploadedFileUrlQuery profileImageUrlQuery,
            UploadedFileUrlQuery introductionImageUrlQuery
    ) {
        return new MyClubInfoQuery(
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
