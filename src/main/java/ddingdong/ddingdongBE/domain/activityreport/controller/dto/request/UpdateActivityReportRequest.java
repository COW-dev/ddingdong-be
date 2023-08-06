package ddingdong.ddingdongBE.domain.activityreport.controller.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import ddingdong.ddingdongBE.domain.activityreport.domain.Participant;
import lombok.Getter;

@Getter
public class UpdateActivityReportRequest {
    private String term;
    private String content;
    private String place;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<Participant> participants;
}
