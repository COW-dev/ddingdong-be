package ddingdong.ddingdongBE.domain.activityreport.service;

import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.ACTIVITY_REPORT;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.IMAGE;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReport;
import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReportTermInfo;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.command.CreateActivityReportCommand;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.command.CreateActivityTermInfoCommand;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.command.UpdateActivityReportCommand;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportInfo;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportListQuery;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportQuery;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportTermInfoQuery;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.fileinformation.service.FileInformationService;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.file.service.FileService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacadeActivityReportService {

    private final ActivityReportService activityReportService;
    private final ActivityReportTermInfoService activityReportTermInfoService;
    private final ClubService clubService;
    private final FileInformationService fileInformationService;
    private final FileService fileService;

    public List<ActivityReportQuery> getActivityReport(
        String term,
        String clubName
    ) {
        List<ActivityReport> activityReports = activityReportService.getActivityReport(term,
            clubName);
        return activityReports.stream()
            .map(this::parseToQuery)
            .toList();
    }

    public List<ActivityReportListQuery> getActivityReports() {
        List<ActivityReport> activityReports = activityReportService.getActivityReports();
        return parseToListQuery(activityReports);
    }

    public List<ActivityReportListQuery> getMyActivityReports(User user) {
        Club club = clubService.getByUserId(user.getId());
        List<ActivityReport> activityReports = activityReportService.getActivityReportsByClub(club);
        return parseToListQuery(activityReports);
    }

    public List<ActivityReportTermInfoQuery> getActivityReportTermInfos() {
        List<ActivityReportTermInfo> termInfos = activityReportTermInfoService.getActivityReportTermInfos();
        return termInfos.stream()
            .map(ActivityReportTermInfoQuery::from)
            .toList();
    }

    public String getCurrentTerm() {
        return activityReportTermInfoService.getCurrentTerm();
    }

    @Transactional
    public void createActivityTermInfo(CreateActivityTermInfoCommand command) {
        activityReportTermInfoService.create(command.startDate(), command.totalTermCount());
    }

    @Transactional
    public void create(
        User user,
        List<CreateActivityReportCommand> commands
    ) {
        Club club = clubService.getByUserId(user.getId());

        commands.forEach(command -> {
            ActivityReport activityReport = command.toEntity(club);
            activityReportService.create(activityReport);
        });
    }

    @Transactional
    public void update(
        User user,
        String term,
        List<UpdateActivityReportCommand> commands
    ) {
        Club club = clubService.getByUserId(user.getId());
        List<ActivityReport> updateActivityReports = commands.stream()
            .map(UpdateActivityReportCommand::toEntity)
            .toList();
        activityReportService.update(club.getName(), term, updateActivityReports);
    }

    @Transactional
    public void delete(User user, String term) {
        Club club = clubService.getByUserId(user.getId());
        List<ActivityReport> activityReports = activityReportService.getActivityReport(
            club.getName(),
            term);
        activityReportService.deleteAll(activityReports);
    }

    public List<ActivityReportInfo> getActivityReportInfos(
        User user,
        String term
    ) {
        Club club = clubService.getByUserId(user.getId());
        List<ActivityReport> activityReports = activityReportService.getActivityReport(
            club.getName(),
            term);

        if (activityReports.isEmpty()) {
            throw new ResourceNotFound("ActivityReport를 찾을 수 없습니다.");
        }

        return activityReports.stream()
            .map(ActivityReportInfo::from)
            .toList();
    }

    @Transactional
    public void uploadImages(List<ActivityReportInfo> activityReportInfos, MultipartFile firstImage,
        MultipartFile secondImage) {
        List<MultipartFile> images = Arrays.asList(firstImage, secondImage);
        IntStream.range(0, activityReportInfos.size())
            .filter(index -> images.get(index) != null && !images.get(index).isEmpty())
            .forEach(index -> {
                fileService.uploadFile(
                    activityReportInfos.get(index).id(),
                    Collections.singletonList(images.get(index)),
                    IMAGE,
                    ACTIVITY_REPORT
                );
            });
    }

    @Transactional
    public void updateImages(List<ActivityReportInfo> activityReportInfos, MultipartFile firstImage,
        MultipartFile secondImage) {
        List<MultipartFile> images = Arrays.asList(firstImage, secondImage);

        IntStream.range(0, activityReportInfos.size())
            .filter(index -> images.get(index) != null && !images.get(index).isEmpty())
            .forEach(index -> {
                    fileService.deleteFile(
                        activityReportInfos.get(index).id(),
                        IMAGE,
                        ACTIVITY_REPORT
                    );

                    fileService.uploadFile(
                        activityReportInfos.get(index).id(),
                        Collections.singletonList(images.get(index)),
                        IMAGE,
                        ACTIVITY_REPORT
                    );
                }
            );
    }

    @Transactional
    public void deleteImages(List<ActivityReportInfo> activityReportInfos) {
        activityReportInfos.forEach(query -> {
            fileService.deleteFile(query.id(), IMAGE, ACTIVITY_REPORT);
        });
    }

    public String getRequestTerm(List<CreateActivityReportCommand> commands) {
        return commands.stream()
            .findFirst()
            .map(CreateActivityReportCommand::term)
            .orElse(null);
    }

    private ActivityReportQuery parseToQuery(ActivityReport activityReport) {
        String imagePath =
            IMAGE.getFileType() + ACTIVITY_REPORT.getFileDomain() + activityReport.getId();
        List<String> imageUrls = fileInformationService.getImageUrls(imagePath);
        return ActivityReportQuery.of(activityReport, imageUrls);
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
