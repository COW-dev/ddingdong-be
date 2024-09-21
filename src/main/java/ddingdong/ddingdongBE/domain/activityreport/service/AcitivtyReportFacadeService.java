package ddingdong.ddingdongBE.domain.activityreport.service;

import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportDto;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportListResponse;
import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReport;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.command.CreateActivityTermInfoCommand;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AcitivtyReportFacadeService {

  private final ActivityReportService activityReportService;
  private final ActivityReportTermInfoService activityReportTermInfoService;

  public List<ActivityReportListResponse> getActivityReports() {
    List<ActivityReport> activityReports = activityReportService.getAll();
    return parseToActivityReportResponse(activityReports);
  }

  public void createActivityTermInfo(CreateActivityTermInfoCommand command) {
    activityReportTermInfoService.create(command.startDate(), command.totalTermCount());
  }

  private List<ActivityReportListResponse> parseToActivityReportResponse(
      final List<ActivityReport> activityReports) {
    Map<String, Map<String, List<Long>>> groupedData = activityReports.stream().collect(
        Collectors.groupingBy(activityReport -> activityReport.getClub().getName(),
            Collectors.groupingBy(ActivityReport::getTerm,
                Collectors.mapping(ActivityReport::getId, Collectors.toList()))));

    return groupedData.entrySet().stream().flatMap(entry -> {
      String clubName = entry.getKey();
      Map<String, List<Long>> termMap = entry.getValue();

      return termMap.entrySet().stream().map(termEntry -> {
        String term = termEntry.getKey();
        List<ActivityReportDto> activityReportDtos = termEntry.getValue().stream()
            .map(ActivityReportDto::new)
            .collect(Collectors.toList());
        return ActivityReportListResponse.of(clubName, term, activityReportDtos);
      });

    }).collect(Collectors.toList());
  }
}
