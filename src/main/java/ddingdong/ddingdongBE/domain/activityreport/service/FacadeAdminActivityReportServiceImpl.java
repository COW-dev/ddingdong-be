package ddingdong.ddingdongBE.domain.activityreport.service;

import ddingdong.ddingdongBE.domain.activityreport.entity.ActivityReport;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.command.CreateActivityTermInfoCommand;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportInfo;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.AdminActivityReportListQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacadeAdminActivityReportServiceImpl implements FacadeAdminActivityReportService {

    private final ActivityReportTermInfoService activityReportTermInfoService;
    private final ActivityReportService activityReportService;

    @Override
    public List<AdminActivityReportListQuery> getActivityReports(LocalDateTime now, int term) {
        int currentYear = now.getYear();
        List<ActivityReport> activityReports = activityReportService.getActivityReports(currentYear, term);
        return parseToListQuery(activityReports);
    }

    @Transactional
    @Override
    public void createActivityTermInfo(CreateActivityTermInfoCommand command) {
        activityReportTermInfoService.create(command.startDate(), command.totalTermCount());
    }

    private List<AdminActivityReportListQuery> parseToListQuery(final List<ActivityReport> activityReports) {
        Map<String, List<ActivityReport>> activityReportsGroupedByClubName = activityReports.stream()
                .collect(Collectors.groupingBy(report -> report.getClub().getName()));

        return activityReportsGroupedByClubName.entrySet().stream()
                .map(entry -> {
                    List<ActivityReportInfo> activityReportInfos = entry.getValue().stream()
                            .map(ActivityReportInfo::from)
                            .toList();
                    return new AdminActivityReportListQuery(entry.getKey(), activityReportInfos);
                })
                .toList();
    }
}
