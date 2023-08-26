package ddingdong.ddingdongBE.domain.activityreport.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

import ddingdong.ddingdongBE.domain.activityreport.domain.Participant;
import lombok.Getter;

@Getter
public class UpdateActivityReportRequest {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";

    private String content;
    private String place;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT, timezone = "Asia/Seoul")
    private LocalDateTime startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT, timezone = "Asia/Seoul")
    private LocalDateTime endDate;
    private List<Participant> participants;
}
