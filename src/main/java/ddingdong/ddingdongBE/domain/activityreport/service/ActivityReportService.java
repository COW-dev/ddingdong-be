package ddingdong.ddingdongBE.domain.activityreport.service;

import ddingdong.ddingdongBE.domain.activityreport.entity.ActivityReport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import java.util.List;

public interface ActivityReportService {

    List<ActivityReport> getActivityReports(int year, int term);

    List<ActivityReport> getActivityReportsByClub(final Club club);

    List<ActivityReport> getActivityReport(String clubName, String term);

    Long create(final ActivityReport activityReport);

    void update(ActivityReport activityReport, ActivityReport updateActivityReport);

    void deleteAll(List<ActivityReport> activityReports);

    List<ActivityReport> getActivityReportOrThrow(String clubName, String term);
}
