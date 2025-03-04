package ddingdong.ddingdongBE.domain.activityreport.service;

import ddingdong.ddingdongBE.domain.activityreport.service.dto.command.CreateActivityReportCommand;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.command.UpdateActivityReportCommand;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportListQuery;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportQuery;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportTermInfoQuery;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;

public interface FacadeClubActivityReportService {

    List<ActivityReportQuery> getActivityReport(String term, String clubName);

    List<ActivityReportListQuery> getMyActivityReports(User user);

    List<ActivityReportTermInfoQuery> getActivityReportTermInfos();

    String getCurrentTerm(LocalDateTime now);

    void create(User user, List<CreateActivityReportCommand> commands);

    void update(User user, String term, List<UpdateActivityReportCommand> commands);

    void delete(User user, String term);

}
