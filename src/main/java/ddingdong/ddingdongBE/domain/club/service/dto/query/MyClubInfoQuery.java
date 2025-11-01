package ddingdong.ddingdongBE.domain.club.service.dto.query;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlAndNameQuery;
import java.time.LocalDate;

public record MyClubInfoQuery(
        String name,
        String category,
        String tag,
        String leader,
        String phoneNumber,
        String location,
        LocalDate startDate,
        LocalDate endDate,
        String regularMeeting,
        String introduction,
        String activity,
        String ideal,
        UploadedFileUrlAndNameQuery profileImageUrlQuery,
        UploadedFileUrlAndNameQuery introductionImageUrlQuery
) {

    public static MyClubInfoQuery of(
            Club club,
            Form form,
            UploadedFileUrlAndNameQuery profileImageUrlQuery,
            UploadedFileUrlAndNameQuery introductionImageUrlQuery
    ) {
        return new MyClubInfoQuery(
                club.getName(),
                club.getCategory(),
                club.getTag(),
                club.getLeader(),
                club.getPhoneNumber().getNumber(),
                club.getLocation().getValue(),
                form != null ? form.getStartDate() : null,
                form != null ? form.getEndDate() : null,
                club.getRegularMeeting(),
                club.getIntroduction(),
                club.getActivity(),
                club.getIdeal(),
                profileImageUrlQuery,
                introductionImageUrlQuery
        );
    }
}
