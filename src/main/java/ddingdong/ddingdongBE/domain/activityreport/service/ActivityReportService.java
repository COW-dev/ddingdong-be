package ddingdong.ddingdongBE.domain.activityreport.service;

import ddingdong.ddingdongBE.domain.activityreport.entity.ActivityReport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import java.util.List;

public interface ActivityReportService {

    List<ActivityReport> getActivityReports(int year, int term);

    List<ActivityReport> getActivityReportsByClub(Club club, int year);

    List<ActivityReport> getActivityReport(Club club, int year, int term);

    Long create(final ActivityReport activityReport);

    void update(ActivityReport activityReport, ActivityReport updateActivityReport);

    void deleteAll(List<ActivityReport> activityReports);

    List<ActivityReport> getActivityReportOrThrow(Club club, int year, int term);
}
