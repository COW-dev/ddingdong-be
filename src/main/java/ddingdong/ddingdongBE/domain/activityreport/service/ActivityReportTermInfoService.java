package ddingdong.ddingdongBE.domain.activityreport.service;

import ddingdong.ddingdongBE.domain.activityreport.entity.ActivityReportTermInfo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

public interface ActivityReportTermInfoService {

    List<ActivityReportTermInfo> getActivityReportTermInfos(Year year);

    void create(LocalDate startDate, int totalTermCount);

    int getCurrentTerm(LocalDateTime now);

    void validateSubmissionPeriod(int term, LocalDate today);
}
