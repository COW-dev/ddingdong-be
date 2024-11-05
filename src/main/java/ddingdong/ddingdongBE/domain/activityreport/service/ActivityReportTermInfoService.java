package ddingdong.ddingdongBE.domain.activityreport.service;

import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReportTermInfo;
import java.time.LocalDate;
import java.util.List;

public interface ActivityReportTermInfoService {

    List<ActivityReportTermInfo> getActivityReportTermInfos();

    void create(LocalDate startDate, int totalTermCount);

    String getCurrentTerm();
}
