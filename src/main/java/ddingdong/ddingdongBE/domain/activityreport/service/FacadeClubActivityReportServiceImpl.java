package ddingdong.ddingdongBE.domain.activityreport.service;

import ddingdong.ddingdongBE.domain.activityreport.entity.ActivityReport;
import ddingdong.ddingdongBE.domain.activityreport.entity.ActivityReportTermInfo;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.command.CreateActivityReportCommand;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.command.UpdateActivityReportCommand;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportInfo;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportListQuery;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportQuery;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportTermInfoQuery;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.file.service.S3FileService;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
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
    public List<ActivityReportQuery> getActivityReport(
            String term,
            String clubName
    ) {
        List<ActivityReport> activityReports = activityReportService.getActivityReport(clubName, term);

        return activityReports.stream()
                .map(this::parseToQuery)
                .toList();
    }

    @Override
    public List<ActivityReportListQuery> getMyActivityReports(User user) {
        Club club = clubService.getByUserId(user.getId());
        List<ActivityReport> activityReports = activityReportService.getActivityReportsByClub(club);
        return parseToListQuery(activityReports);
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
            String term,
            List<UpdateActivityReportCommand> commands
    ) {
        Club club = clubService.getByUserId(user.getId());
        List<ActivityReport> activityReports = activityReportService.getActivityReportOrThrow(club.getName(), term);

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
    public void delete(User user, String term) {
        Club club = clubService.getByUserId(user.getId());
        List<ActivityReport> activityReports = activityReportService.getActivityReportOrThrow(club.getName(), term);
        activityReportService.deleteAll(activityReports);
        activityReports.forEach(activityReport -> {
            fileMetaDataService.updateStatusToDelete(DomainType.ACTIVITY_REPORT_IMAGE, activityReport.getId());
        });
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
