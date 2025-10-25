package ddingdong.ddingdongBE.domain.activityreport.service.dto.command;

import ddingdong.ddingdongBE.common.utils.TimeUtils;
import ddingdong.ddingdongBE.domain.activityreport.entity.ActivityReport;
import ddingdong.ddingdongBE.domain.activityreport.entity.Participant;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import java.util.List;
import lombok.Builder;

@Builder
public record CreateActivityReportCommand(
    int term,
    String content,
    String place,
    String startDate,
    String endDate,
    String imageId,
    List<Participant> participants
) {

    public ActivityReport toEntity(Club club) {
        return ActivityReport.builder()
            .term(term)
            .content(content)
            .place(place)
            .startDate(TimeUtils.parseToLocalDateTime(startDate))
            .endDate(TimeUtils.parseToLocalDateTime(endDate))
            .participants(participants)
            .club(club)
            .build();
    }
}
