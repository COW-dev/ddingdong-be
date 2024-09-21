package ddingdong.ddingdongBE.domain.activityreport.service;

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
      final String term,
      final String clubName
  ) {
    return activityReportRepository.findByClubNameAndTerm(clubName, term);
  }

  @Transactional
  public Long create(final ActivityReport activityReport) {
    ActivityReport savedActivityReport = activityReportRepository.save(activityReport);
    return savedActivityReport.getId();
  }

  @Transactional
  public void update(
      final String clubName,
      final String term,
      final List<ActivityReport> updateActivityReports
  ) {
    List<ActivityReport> activityReports = getActivityReport(term, clubName);

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
}
