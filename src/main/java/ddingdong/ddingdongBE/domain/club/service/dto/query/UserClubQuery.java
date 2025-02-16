package ddingdong.ddingdongBE.domain.club.service.dto.query;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import java.time.LocalDate;

public record UserClubQuery(
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
        Long formId,
        UploadedFileUrlQuery profileImageUrlQuery,
        UploadedFileUrlQuery introductionImageUrlQuery
) {

    public static UserClubQuery of(
            Club club,
            Form form,
            UploadedFileUrlQuery profileImageUrlQuery,
            UploadedFileUrlQuery introductionImageUrlQuery) {
        return new UserClubQuery(
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
                form != null ? form.getId() : null,
                profileImageUrlQuery,
                introductionImageUrlQuery
        );
    }

}
