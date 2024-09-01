package ddingdong.ddingdongBE.domain.activityreport.controller.dto.request;

import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReport;
import ddingdong.ddingdongBE.domain.activityreport.domain.Participant;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@AllArgsConstructor
public class CreateActivityReportRequest {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";

    private String term;
    private String content;
    private String place;

    @DateTimeFormat(pattern = "yyyy-mm-dd HH:mm")
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-mm-dd HH:mm")
    private LocalDateTime endDate;

    private List<Participant> participants;

    public ActivityReport toEntity(Club club) {
        return ActivityReport.builder()
                .term(this.term)
                .content(this.content)
                .place(this.place)
                .startDate(parseToDate(this.getStartDate()))
                .endDate(parseToDate(this.getEndDate()))
                .participants(this.participants)
                .club(club)
                .build();
    }

    private LocalDateTime parseToDate(final LocalDateTime date) {
        String dateString = date.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        return LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern(DATE_FORMAT));
    }
}
