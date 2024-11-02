package ddingdong.ddingdongBE.domain.activityreport.service;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReport;
import ddingdong.ddingdongBE.domain.activityreport.repository.ActivityReportRepository;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ActivityReportService {

    private final ActivityReportRepository activityReportRepository;

    public List<ActivityReport> getActivityReports() {
        return activityReportRepository.findAll();
    }

    public List<ActivityReport> getActivityReportsByClub(final Club club) {
        return activityReportRepository.findByClubName(club.getName());
    }

    public List<ActivityReport> getActivityReport(
        final String clubName,
        final String term
    ) {
        return activityReportRepository.findByClubNameAndTerm(clubName, term);
    }

    @Transactional
    public Long create(final ActivityReport activityReport) {
        ActivityReport createdActivityReport = activityReportRepository.save(activityReport);
        return createdActivityReport.getId();
    }

    @Transactional
    public void update(
        final ActivityReport activityReport,
        final ActivityReport updateActivityReport
    ) {
        activityReport.update(updateActivityReport);
    }

    @Transactional
    public void deleteAll(List<ActivityReport> activityReports) {
        activityReportRepository.deleteAll(activityReports);
    }

    public List<ActivityReport> getActivityReportOrThrow(String clubName, String term) {
        List<ActivityReport> activityReports = getActivityReport(clubName, term);
        if (activityReports.isEmpty()) {
            throw new ResourceNotFound("해당 ActivityReports(clubName: " + clubName + ", term: " + term + ")"
                + "를 찾을 수 없습니다.");
        }
        return activityReports;
    }
}
