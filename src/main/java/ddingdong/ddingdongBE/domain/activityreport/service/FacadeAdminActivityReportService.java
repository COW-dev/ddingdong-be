package ddingdong.ddingdongBE.domain.activityreport.service;

import ddingdong.ddingdongBE.domain.activityreport.service.dto.command.CreateActivityTermInfoCommand;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportListQuery;
import java.util.List;

public interface FacadeAdminActivityReportService {

    List<ActivityReportListQuery> getActivityReports();

    void createActivityTermInfo(CreateActivityTermInfoCommand command);
}
