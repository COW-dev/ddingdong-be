package ddingdong.ddingdongBE.domain.activityreport.controller.dto.request;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class ActivityReportDateRequest {

    private LocalDate startDate;

    private LocalDate endDate;


}
