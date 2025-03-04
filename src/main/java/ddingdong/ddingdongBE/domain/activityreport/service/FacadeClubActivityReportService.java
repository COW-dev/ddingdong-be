package ddingdong.ddingdongBE.domain.activityreport.service;

import ddingdong.ddingdongBE.domain.activityreport.service.dto.command.CreateActivityReportCommand;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.command.UpdateActivityReportCommand;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.AdminActivityReportListQuery;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportQuery;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportTermInfoQuery;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.CentralActivityReportListQuery;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;

public interface FacadeClubActivityReportService {

    List<ActivityReportQuery> getActivityReport(User user,  LocalDateTime now, String term);

    List<CentralActivityReportListQuery> getMyActivityReports(User user, LocalDateTime now);

    List<ActivityReportTermInfoQuery> getActivityReportTermInfos();

    String getCurrentTerm(LocalDateTime now);

    void create(User user, List<CreateActivityReportCommand> commands);

    void update(User user, String term, LocalDateTime now, List<UpdateActivityReportCommand> commands);

    void delete(User user, String term, LocalDateTime now);

}
