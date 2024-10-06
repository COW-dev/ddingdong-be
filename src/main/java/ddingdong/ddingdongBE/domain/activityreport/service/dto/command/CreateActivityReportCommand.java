package ddingdong.ddingdongBE.domain.activityreport.service.dto.command;

import ddingdong.ddingdongBE.common.utils.TimeParser;
import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReport;
import ddingdong.ddingdongBE.domain.activityreport.domain.Participant;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import java.util.List;
import lombok.Builder;

@Builder
public record CreateActivityReportCommand(
    String term,
    String content,
    String place,
    String startDate,
    String endDate,
    List<Participant> participants
) {

    public ActivityReport toEntity(Club club) {
        return ActivityReport.builder()
            .term(term)
            .content(content)
            .place(place)
            .startDate(TimeParser.parseToLocalDateTime(startDate))
            .endDate(TimeParser.parseToLocalDateTime(endDate))
            .participants(participants)
            .club(club)
            .build();
    }
}
