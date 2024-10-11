package ddingdong.ddingdongBE.domain.club.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import ddingdong.ddingdongBE.domain.club.service.dto.query.UserClubQuery;
import java.time.LocalDateTime;
import java.util.List;

public record UserClubResponse(
        String name,
        String category,
        String tag,
        String leader,
        String phoneNumber,
        String location,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        LocalDateTime startRecruitPeriod,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        LocalDateTime endRecruitPeriod,
        String regularMeeting,
        String introduction,
        String activity,
        String ideal,
        String formUrl,
        List<String> profileImageUrls,
        List<String> introduceImageUrls
) {

    public static UserClubResponse from(UserClubQuery query) {
        return new UserClubResponse(
                query.name(),
                query.category(),
                query.tag(),
                query.leader(),
                query.phoneNumber(),
                query.location(),
                query.startRecruitPeriod(),
                query.endRecruitPeriod(),
                query.regularMeeting(),
                query.introduction(),
                query.activity(),
                query.ideal(),
                query.formUrl(),
                query.profileImageUrls(),
                query.introduceImageUrls()
        );
    }
}
