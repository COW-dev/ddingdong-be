package ddingdong.ddingdongBE.domain.activityreport.controller.dto.request;

import ddingdong.ddingdongBE.domain.activityreport.domain.Participant;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;

@Getter
public class UpdateActivityReportRequest {
    private String term;
    private String content;
    private String place;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Participant> participants;
}
