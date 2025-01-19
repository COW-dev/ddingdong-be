package ddingdong.ddingdongBE.domain.activityreport.service;

import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReport;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.command.CreateActivityTermInfoCommand;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportInfo;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportListQuery;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacadeAdminActivityReportServiceImpl implements FacadeAdminActivityReportService{

    private final ActivityReportTermInfoService activityReportTermInfoService;
    private final ActivityReportService activityReportService;

    @Override
    public List<ActivityReportListQuery> getActivityReports() {
        List<ActivityReport> activityReports = activityReportService.getActivityReports();
        return parseToListQuery(activityReports);
    }

    @Transactional
    @Override
    public void createActivityTermInfo(CreateActivityTermInfoCommand command) {
        activityReportTermInfoService.create(command.startDate(), command.totalTermCount());
    }

    private List<ActivityReportListQuery> parseToListQuery(final List<ActivityReport> activityReports) {
        Map<String, Map<String, List<Long>>> groupedData = activityReports.stream().collect(
            Collectors.groupingBy(activityReport -> activityReport.getClub().getName(),
                Collectors.groupingBy(ActivityReport::getTerm,
                    Collectors.mapping(ActivityReport::getId, Collectors.toList()))));

        return groupedData.entrySet().stream().flatMap(entry -> {
            String clubName = entry.getKey();
            Map<String, List<Long>> termMap = entry.getValue();

            return termMap.entrySet().stream().map(termEntry -> {
                String term = termEntry.getKey();
                List<ActivityReportInfo> activityReportInfos = termEntry.getValue().stream()
                    .map(ActivityReportInfo::new)
                    .toList();
                return ActivityReportListQuery.of(clubName, term, activityReportInfos);
            });
        }).toList();
    }
}
