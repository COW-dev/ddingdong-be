package ddingdong.ddingdongBE.domain.club.controller.dto.request;

import ddingdong.ddingdongBE.domain.club.service.dto.command.UpdateClubInfoCommand;
import java.util.List;

public record UpdateClubInfoRequest(
        String name,
        String category,
        String tag,
        String content,
        String clubLeader,
        String phoneNumber,
        String location,
        String startRecruitPeriod,
        String endRecruitPeriod,
        String regularMeeting,
        String introduction,
        String activity,
        String ideal,
        String formUrl,
        List<String> profileImageUrls,
        List<String> introduceImageUrls

) {

    public UpdateClubInfoCommand toCommand(Long userId) {
        return new UpdateClubInfoCommand(
                userId,
                name,
                category,
                tag,
                clubLeader,
                phoneNumber,
                location,
                startRecruitPeriod,
                endRecruitPeriod,
                regularMeeting,
                introduction,
                activity,
                ideal,
                formUrl,
                profileImageUrls,
                introduceImageUrls
        );
    }
}
