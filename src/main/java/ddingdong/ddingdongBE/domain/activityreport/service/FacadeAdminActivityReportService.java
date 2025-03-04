package ddingdong.ddingdongBE.domain.activityreport.service;

import ddingdong.ddingdongBE.domain.activityreport.service.dto.command.CreateActivityTermInfoCommand;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.AdminActivityReportListQuery;
import java.time.LocalDateTime;
import java.util.List;

public interface FacadeAdminActivityReportService {

    List<AdminActivityReportListQuery> getActivityReports(LocalDateTime now, int term);

    void createActivityTermInfo(CreateActivityTermInfoCommand command);
}
