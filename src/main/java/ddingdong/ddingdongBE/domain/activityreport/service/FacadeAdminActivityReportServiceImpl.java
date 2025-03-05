package ddingdong.ddingdongBE.domain.activityreport.service;

import ddingdong.ddingdongBE.domain.activityreport.entity.ActivityReport;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.command.CreateActivityTermInfoCommand;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportInfo;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportQuery;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.AdminActivityReportListQuery;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.file.service.S3FileService;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
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

    private final ClubService clubService;
    private final ActivityReportTermInfoService activityReportTermInfoService;
    private final ActivityReportService activityReportService;
    private final FileMetaDataService fileMetaDataService;
    private final S3FileService s3FileService;

    @Override
    public List<AdminActivityReportListQuery> getActivityReports(LocalDateTime now, int term) {
        int currentYear = now.getYear();
        List<ActivityReport> activityReports = activityReportService.getActivityReports(currentYear, term);
        return parseToListQuery(activityReports);
    }

    @Override
    public List<ActivityReportQuery> getActivityReport(Long clubId, LocalDateTime now, int term) {
        Club club = clubService.getById(clubId);
        int currentYear = now.getYear();
        List<ActivityReport> activityReports = activityReportService.getActivityReport(club, currentYear, term);

        return activityReports.stream()
                .map(this::parseToQuery)
                .toList();
    }

    @Transactional
    @Override
    public void createActivityTermInfo(CreateActivityTermInfoCommand command) {
        activityReportTermInfoService.create(command.startDate(), command.totalTermCount());
    }

    private ActivityReportQuery parseToQuery(ActivityReport activityReport) {
        UploadedFileUrlQuery image = fileMetaDataService
                .getCoupledAllByDomainTypeAndEntityId(DomainType.ACTIVITY_REPORT_IMAGE, activityReport.getId())
                .stream()
                .map(fileMetaData -> s3FileService.getUploadedFileUrl(fileMetaData.getFileKey()))
                .findFirst()
                .orElse(null);
        return ActivityReportQuery.of(activityReport, image);
    }

    private List<AdminActivityReportListQuery> parseToListQuery(final List<ActivityReport> activityReports) {
        Map<Club, List<ActivityReport>> activityReportsGroupedByClubName = activityReports.stream()
                .collect(Collectors.groupingBy(ActivityReport::getClub));

        return activityReportsGroupedByClubName.entrySet().stream()
                .map(entry -> {
                    List<ActivityReportInfo> activityReportInfos = entry.getValue().stream()
                            .map(ActivityReportInfo::from)
                            .toList();
                    return AdminActivityReportListQuery.of(entry.getKey(), activityReportInfos);
                })
                .toList();
    }
}
