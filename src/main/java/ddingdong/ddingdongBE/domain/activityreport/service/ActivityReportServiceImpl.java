package ddingdong.ddingdongBE.domain.activityreport.service;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.activityreport.entity.ActivityReport;
import ddingdong.ddingdongBE.domain.activityreport.repository.ActivityReportRepository;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ActivityReportServiceImpl implements ActivityReportService {

    private final ActivityReportRepository activityReportRepository;

    @Override
    public List<ActivityReport> getActivityReports(int year, int term) {
        return activityReportRepository.findAllByCurrentYearAndTerm(year, term);
    }

    @Override
    public List<ActivityReport> getActivityReportsByClub(Club club, int year) {
        return activityReportRepository.findAllByClub(club, year);
    }

    @Override
    public List<ActivityReport> getActivityReport(Club club, int year, String term) {
        return activityReportRepository.findByClubAndTerm(club, year, term);
    }

    @Transactional
    @Override
    public Long create(final ActivityReport activityReport) {
        ActivityReport createdActivityReport = activityReportRepository.save(activityReport);
        return createdActivityReport.getId();
    }

    @Transactional
    @Override
    public void update(
            final ActivityReport activityReport,
            final ActivityReport updateActivityReport
    ) {
        activityReport.update(updateActivityReport);
    }

    @Transactional
    @Override
    public void deleteAll(List<ActivityReport> activityReports) {
        activityReportRepository.deleteAll(activityReports);
    }

    @Override
    public List<ActivityReport> getActivityReportOrThrow(Club club, int year, String term) {
        List<ActivityReport> activityReports = getActivityReport(club, year, term);
        if (activityReports.isEmpty()) {
            throw new ResourceNotFound("해당 ActivityReports(clubName: " + club.getName() + ", term: " + term + ")"
                    + "를 찾을 수 없습니다.");
        }
        return activityReports;
    }
}
