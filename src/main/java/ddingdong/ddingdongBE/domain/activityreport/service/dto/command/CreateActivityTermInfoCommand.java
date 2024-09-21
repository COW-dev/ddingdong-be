package ddingdong.ddingdongBE.domain.activityreport.service.dto.command;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record CreateActivityTermInfoCommand(
    LocalDate startDate,
    int totalTermCount
) {

}
