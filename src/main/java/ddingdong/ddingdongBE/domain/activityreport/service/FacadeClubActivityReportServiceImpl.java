package ddingdong.ddingdongBE.domain.activityreport.service;

import ddingdong.ddingdongBE.domain.activityreport.entity.ActivityReport;
import ddingdong.ddingdongBE.domain.activityreport.entity.ActivityReportTermInfo;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.command.CreateActivityReportCommand;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.command.UpdateActivityReportCommand;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportInfo;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportQuery;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportTermInfoQuery;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.CentralActivityReportListQuery;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.file.service.S3FileService;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlAndNameQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacadeClubActivityReportServiceImpl implements FacadeClubActivityReportService {

    private final ActivityReportService activityReportService;
    private final ActivityReportTermInfoService activityReportTermInfoService;
    private final ClubService clubService;
    private final FileMetaDataService fileMetaDataService;
    private final S3FileService s3FileService;

    @Override
    public List<ActivityReportQuery> getActivityReport(User user, LocalDateTime now, int term) {
        Club club = clubService.getByUserId(user.getId());
        int currentYear = now.getYear();
        List<ActivityReport> activityReports = activityReportService.getActivityReport(club, currentYear, term);

        return activityReports.stream()
                .map(this::parseToQuery)
                .toList();
    }

    @Override
    public List<CentralActivityReportListQuery> getMyActivityReports(User user, LocalDateTime now) {
        Club club = clubService.getByUserId(user.getId());
        int currentYear = now.getYear();
        List<ActivityReport> activityReports = activityReportService.getActivityReportsByClub(club, currentYear);
        return parseToListQuery(club.getName(), activityReports);
    }

    @Override
    public List<ActivityReportTermInfoQuery> getActivityReportTermInfos() {
        List<ActivityReportTermInfo> termInfos = activityReportTermInfoService.getActivityReportTermInfos();
        return termInfos.stream()
                .map(ActivityReportTermInfoQuery::from)
                .toList();
    }

    @Override
    public String getCurrentTerm(LocalDateTime now) {
        return activityReportTermInfoService.getCurrentTerm(now);
    }

    @Transactional
    @Override
    public void create(
            User user,
            List<CreateActivityReportCommand> commands
    ) {
        Club club = clubService.getByUserId(user.getId());
        commands.forEach(command -> {
            ActivityReport activityReport = command.toEntity(club);
            Long activityId = activityReportService.create(activityReport);

            fileMetaDataService.updateStatusToCoupled(command.imageId(), DomainType.ACTIVITY_REPORT_IMAGE, activityId);
        });
    }

    @Transactional
    @Override
    public void update(
            User user,
            LocalDateTime now,
            int term,
            List<UpdateActivityReportCommand> commands
    ) {
        Club club = clubService.getByUserId(user.getId());
        int currentYear = now.getYear();
        List<ActivityReport> activityReports = activityReportService.getActivityReportOrThrow(club, currentYear, term);

        IntStream.range(0, commands.size())
                .forEach(index -> {
                    ActivityReport activityReport = activityReports.get(index);
                    ActivityReport updateActivityReport = commands.get(index).toEntity();
                    activityReportService.update(activityReport, updateActivityReport);

                    fileMetaDataService.update(
                            commands.get(index).imageId(),
                            DomainType.ACTIVITY_REPORT_IMAGE,
                            activityReport.getId()
                    );
                });
    }

    @Transactional
    @Override
    public void delete(User user, LocalDateTime now, int term) {
        Club club = clubService.getByUserId(user.getId());
        int currentYear = now.getYear();
        List<ActivityReport> activityReports = activityReportService.getActivityReportOrThrow(club, currentYear, term);
        activityReportService.deleteAll(activityReports);
        activityReports.forEach(
                activityReport -> fileMetaDataService.updateStatusToDelete(DomainType.ACTIVITY_REPORT_IMAGE,
                        activityReport.getId()));
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

    private List<CentralActivityReportListQuery> parseToListQuery(String clubName,
            List<ActivityReport> activityReports) {
        Map<String, List<ActivityReport>> activityReportsGroupedByTerm = activityReports.stream()
                .collect(Collectors.groupingBy(ActivityReport::getTerm));

        return activityReportsGroupedByTerm.entrySet().stream()
                .map(entry -> {
                    List<ActivityReportInfo> activityReportInfos = entry.getValue().stream()
                            .map(ActivityReportInfo::from)
                            .toList();
                    return CentralActivityReportListQuery.of(clubName, entry.getKey(), activityReportInfos);
                })
                .toList();
    }
}
