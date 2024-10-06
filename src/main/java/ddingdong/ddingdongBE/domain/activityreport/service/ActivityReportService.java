package ddingdong.ddingdongBE.domain.activityreport.service;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReport;
import ddingdong.ddingdongBE.domain.activityreport.repository.ActivityReportRepository;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import java.util.List;
import java.util.stream.IntStream;
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
    public void create(final ActivityReport activityReport) {
        activityReportRepository.save(activityReport);
    }

    @Transactional
    public void update(
        final List<ActivityReport> activityReports,
        final List<ActivityReport> updateActivityReports
    ) {
        IntStream.range(0, updateActivityReports.size())
            .forEach(index -> {
                ActivityReport activityReport = activityReports.get(index);
                ActivityReport updatedActivityReport = updateActivityReports.get(index);
                activityReport.update(updatedActivityReport);
            });
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
