package ddingdong.ddingdongBE.domain.activityreport.controller.dto.request;
import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReport;
import ddingdong.ddingdongBE.domain.activityreport.domain.Participant;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import java.util.List;

import lombok.Getter;

@Getter
public class RegisterActivityReportRequest {

    private String term;
    private String content;
    private String place;
    private ActivityReportDateRequest date;
    private List<Participant> participants;

    public ActivityReport toEntity(Club club) {
        return ActivityReport.builder()
                .term(this.term)
                .content(this.content)
                .place(this.place)
                .startDate(this.date.getStartDate())
                .endDate(this.date.getEndDate())
                .participants(this.participants)
                .club(club)
                .build();
    }
}
