package ddingdong.ddingdongBE.domain.activityreport.service;

import ddingdong.ddingdongBE.common.exception.ActivityReportException;
import ddingdong.ddingdongBE.domain.activityreport.entity.ActivityReport;
import ddingdong.ddingdongBE.domain.activityreport.entity.ActivityReportTermInfo;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.command.CreateActivityTermInfoCommand;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.command.UpdateActivityReportCommand;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportInfo;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportQuery;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.AdminActivityReportListQuery;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.file.service.S3FileService;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlAndNameQuery;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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

    @Transactional
    @Override
    public void deleteActivityTermInfo(LocalDateTime now, int term) {
        int currentYear = now.getYear();
        // 회차 행을 먼저 잠가 같은 회차에 대한 보고서 제출(공유 락)과 순서대로 처리되게 한다.
        List<ActivityReportTermInfo> termInfos =
                activityReportTermInfoService.getAllByYearAndTermWithLock(Year.of(currentYear), term);
        if (!activityReportService.getActivityReports(currentYear, term).isEmpty()) {
            throw new ActivityReportException.SubmittedTermDeleteException();
        }
        activityReportTermInfoService.deleteAll(termInfos);
    }

    @Transactional
    @Override
    public void updateActivityReport(
            Long clubId,
            LocalDateTime now,
            int term,
            List<UpdateActivityReportCommand> commands
    ) {
        Club club = clubService.getById(clubId);
        int currentYear = now.getYear();
        List<ActivityReport> activityReports = activityReportService.getActivityReportOrThrow(club, currentYear, term);
        if (commands.size() > activityReports.size()) {
            throw new ActivityReportException.ExceedingUpdateCountException();
        }

        IntStream.range(0, commands.size())
                .forEach(index -> {
                    ActivityReport activityReport = activityReports.get(index);
                    UpdateActivityReportCommand command = commands.get(index);
                    activityReportService.update(activityReport, command.toEntity());
                    fileMetaDataService.update(command.imageId(), DomainType.ACTIVITY_REPORT_IMAGE,
                            activityReport.getId());
                });
    }

    @Transactional
    @Override
    public void deleteActivityReport(Long clubId, LocalDateTime now, int term) {
        Club club = clubService.getById(clubId);
        int currentYear = now.getYear();
        List<ActivityReport> activityReports = activityReportService.getActivityReportOrThrow(club, currentYear, term);
        activityReportService.deleteAll(activityReports);
        activityReports.forEach(activityReport -> fileMetaDataService.updateStatusToDelete(
                DomainType.ACTIVITY_REPORT_IMAGE, activityReport.getId()));
    }

    private ActivityReportQuery parseToQuery(ActivityReport activityReport) {
        UploadedFileUrlAndNameQuery image = fileMetaDataService
                .getCoupledAllByDomainTypeAndEntityId(DomainType.ACTIVITY_REPORT_IMAGE, activityReport.getId())
                .stream()
                .map(fileMetaData -> s3FileService.getUploadedFileUrlAndName(fileMetaData.getFileKey(), fileMetaData.getFileName()))
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
