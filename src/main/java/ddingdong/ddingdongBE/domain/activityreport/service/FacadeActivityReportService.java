package ddingdong.ddingdongBE.domain.activityreport.service;

import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.ACTIVITY_REPORT;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.IMAGE;

import ddingdong.ddingdongBE.domain.activityreport.controller.ClubActivityReportApiController;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.request.CreateActivityReportRequest;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.request.UpdateActivityReportRequest;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportDto;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportListResponse;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportResponse;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportTermInfoResponse;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.CurrentTermResponse;
import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReport;
import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReportTermInfo;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.command.CreateActivityTermInfoCommand;
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
  private final ClubActivityReportApiController clubActivityReportApiController;

  public List<ActivityReportResponse> getActivityReport(
      String term,
      String clubName
  ) {
    List<ActivityReport> activityReports = activityReportService.getActivityReport(term, clubName);
    return activityReports.stream()
        .map(this::parseToResponse)
        .toList();
  }

  public List<ActivityReportListResponse> getActivityReports() {
    List<ActivityReport> activityReports = activityReportService.getActivityReports();
    return parseToListResponse(activityReports);
  }

  public List<ActivityReportListResponse> getMyActivityReports(User user) {
    Club club = clubService.getByUserId(user.getId());
    List<ActivityReport> activityReports = activityReportService.getActivityReportsByClub(club);
    return parseToListResponse(activityReports);
  }

  public List<ActivityReportTermInfoResponse> getActivityReportTermInfos() {
    List<ActivityReportTermInfo> termInfos = activityReportTermInfoService.getActivityReportTermInfos();
    return termInfos.stream()
        .map(termInfo -> new ActivityReportTermInfoResponse(
            termInfo.getTerm(),
            termInfo.getStartDate(),
            termInfo.getEndDate()
        ))
        .toList();
  }

  public CurrentTermResponse getCurrentTerm() {
    String currentTerm = activityReportTermInfoService.getCurrentTerm();
    return CurrentTermResponse.from(currentTerm);
  }

  @Transactional
  public void createActivityTermInfo(CreateActivityTermInfoCommand command) {
    activityReportTermInfoService.create(command.startDate(), command.totalTermCount());
  }

  @Transactional
  public void create(
      User user,
      List<CreateActivityReportRequest> requests
  ) {
    Club club = clubService.getByUserId(user.getId());

    requests.forEach(request -> {
      ActivityReport activityReport = request.toEntity(club);
      activityReportService.create(activityReport);
    });
  }

  @Transactional
  public void update(
      User user,
      String term,
      List<UpdateActivityReportRequest> requests
  ) {
    Club club = clubService.getByUserId(user.getId());
    List<ActivityReport> updateActivityReports = requests.stream()
        .map(UpdateActivityReportRequest::toEntity)
        .toList();
    activityReportService.update(club.getName(), term, updateActivityReports);
  }

  @Transactional
  public void delete(User user, String term) {
    Club club = clubService.getByUserId(user.getId());
    List<ActivityReport> activityReports = activityReportService.getActivityReport(club.getName(), term);
    activityReportService.deleteAll(activityReports);
  }

  public List<ActivityReportDto> getActivityReportDtos(
      User user,
      String term
  ) {
    Club club = clubService.getByUserId(user.getId());
    List<ActivityReport> activityReports = activityReportService.getActivityReport(club.getName(),
        term);

    return activityReports.stream()
        .map(ActivityReportDto::from)
        .toList();
  }

  public List<ActivityReportDto> getActivityReportDtos(
      User user,
      List<CreateActivityReportRequest> requests
  ) {
    String term = getRequestTerm(requests);
    return getActivityReportDtos(user, term);
  }

  @Transactional
  public void uploadImages(List<ActivityReportDto> activityReportDtos, MultipartFile firstImage,
      MultipartFile secondImage) {
    List<MultipartFile> images = Arrays.asList(firstImage, secondImage);
    IntStream.range(0, activityReportDtos.size())
        .forEach(index -> {
          if (index < images.size() && images.get(index) != null && !images.get(index).isEmpty()) {
            fileService.uploadFile(
                activityReportDtos.get(index).id(),
                Collections.singletonList(images.get(index)),
                IMAGE,
                ACTIVITY_REPORT
            );
          }
        });
  }

  @Transactional
  public void updateImages(List<ActivityReportDto> activityReportDtos, MultipartFile firstImage,
      MultipartFile secondImage) {
    List<MultipartFile> images = Arrays.asList(firstImage, secondImage);

    IntStream.range(0, activityReportDtos.size())
        .filter(index -> images.get(index) != null && !images.get(index).isEmpty())
        .forEach(index -> {
              fileService.deleteFile(
                  activityReportDtos.get(index).id(),
                  IMAGE,
                  ACTIVITY_REPORT
              );

              fileService.uploadFile(
                  activityReportDtos.get(index).id(),
                  Collections.singletonList(images.get(index)),
                  IMAGE,
                  ACTIVITY_REPORT
              );
            }
        );
  }

  @Transactional
  public void deleteImages(List<ActivityReportDto> activityReportDtos) {
    activityReportDtos.forEach(activityReportDto -> {
      fileService.deleteFile(activityReportDto.id(), IMAGE, ACTIVITY_REPORT);
    });
  }

  private String getRequestTerm(List<CreateActivityReportRequest> requests) {
    return requests.stream()
        .findFirst()
        .map(CreateActivityReportRequest::term)
        .orElse(null);
  }

  private ActivityReportResponse parseToResponse(ActivityReport activityReport) {
    String imagePath =
        IMAGE.getFileType() + ACTIVITY_REPORT.getFileDomain() + activityReport.getId();
    List<String> imageUrls = fileInformationService.getImageUrls(imagePath);
    return ActivityReportResponse.of(activityReport, imageUrls);
  }

  private List<ActivityReportListResponse> parseToListResponse(
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
