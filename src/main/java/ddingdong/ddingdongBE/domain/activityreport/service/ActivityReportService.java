package ddingdong.ddingdongBE.domain.activityreport.service;

import static ddingdong.ddingdongBE.domain.imageinformation.entity.ImageCategory.ACTIVITY_REPORT;

import ddingdong.ddingdongBE.domain.activityreport.controller.dto.request.RegisterActivityReportRequest;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.request.UpdateActivityReportRequest;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportDto;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.AllActivityReportResponse;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.CurrentTermResponse;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.DetailActivityReportResponse;
import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReport;
import ddingdong.ddingdongBE.domain.activityreport.repository.ActivityReportRepository;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.imageinformation.service.ImageInformationService;
import ddingdong.ddingdongBE.domain.user.entity.User;

import java.time.Duration;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ActivityReportService {

    private static final String START_DATE = "2023-09-04";
    private static final int DEFAULT_TERM = 1;
    private static final int CORRECTION_VALUE = 1;
    private static final int TERM_LENGTH_OF_DAYS = 14;


    private final ClubService clubService;
    private final ImageInformationService imageInformationService;
    private final ActivityReportRepository activityReportRepository;

    @Transactional(readOnly = true)
    public List<AllActivityReportResponse> getAll() {
        List<ActivityReport> activityReports = activityReportRepository.findAll();

        return parseToActivityReportResponse(activityReports);
    }

    @Transactional(readOnly = true)
    public List<AllActivityReportResponse> getMyActivityReports(final User user) {
        Club club = clubService.findClubByUserId(user.getId());

        List<ActivityReport> activityReports = activityReportRepository.findByClubName(club.getName());

        return parseToActivityReportResponse(activityReports);
    }

//    @Transactional(readOnly = true)
//    public List<DetailActivityReportResponse> getActivityReport(final String term, final String clubName) {
//        List<ActivityReport> activityReports = activityReportRepository.findByClubNameAndTerm(clubName, term);
//
//        List<String> imageUrls = new ArrayList<>();
//
//        return activityReports.stream().map(activityReport -> {
//            imageUrls.addAll(
//                    imageInformationService.getImageUrls(ACTIVITY_REPORT.getFilePath() + activityReport.getId()));
//            return DetailActivityReportResponse.from(activityReport, imageUrls);
//        }).collect(Collectors.toList());
//    }

    @Transactional(readOnly = true)
    public List<DetailActivityReportResponse> getActivityReport(final String term, final String clubName) {
        List<ActivityReport> activityReports = activityReportRepository.findByClubNameAndTerm(clubName, term);

        return activityReports.stream().map(activityReport -> {
            List<String> imageUrls = imageInformationService.getImageUrls(ACTIVITY_REPORT.getFilePath() + activityReport.getId());
            return DetailActivityReportResponse.from(activityReport, imageUrls);
        }).collect(Collectors.toList());
    }

    public Long register(final User user, final RegisterActivityReportRequest registerActivityReportRequest) {

        Club club = clubService.findClubByUserId(user.getId());
        ActivityReport activityReport = registerActivityReportRequest.toEntity(club);

        ActivityReport savedActivityReport = activityReportRepository.save(activityReport);

        return savedActivityReport.getId();
    }

    public List<ActivityReportDto> update(final User user, final String term,
                                          final List<UpdateActivityReportRequest> requests) {

        Club club = clubService.findClubByUserId(user.getId());

        List<ActivityReport> activityReports = activityReportRepository.findByClubNameAndTerm(club.getName(), term);

        return IntStream.range(0, activityReports.size()).mapToObj(index -> {
            activityReports.get(index).update(requests.get(index));
            return ActivityReportDto.from(activityReports.get(index));
        }).collect(Collectors.toList());
    }

    public List<ActivityReportDto> delete(final User user, final String term) {
        Club club = clubService.findClubByUserId(user.getId());

        List<ActivityReport> activityReports = activityReportRepository.findByClubNameAndTerm(club.getName(), term);

        return activityReports.stream()
                .peek(activityReport -> activityReport.getParticipants().clear())
                .peek(activityReportRepository::delete).map(ActivityReportDto::from).collect(Collectors.toList());
    }

    public CurrentTermResponse getCurrentTerm() {
        LocalDate startDate = LocalDate.parse(START_DATE);
        LocalDate currentDate = LocalDate.now();

        int gapOfDays = calculateGapOfDays(startDate, currentDate);
        return CurrentTermResponse.of(calculateCurrentTerm(gapOfDays));
    }

    private int calculateGapOfDays(final LocalDate startDate, final LocalDate currentDate) {
        return (int) Duration.between(startDate.atStartOfDay(), currentDate.atStartOfDay()).toDays();
    }

    private String calculateCurrentTerm(final int days) {
        int result = CORRECTION_VALUE + (days / TERM_LENGTH_OF_DAYS);

        if (result <= 0) {
            result = DEFAULT_TERM;
        }

        return String.valueOf(result);
    }

    private List<AllActivityReportResponse> parseToActivityReportResponse(final List<ActivityReport> activityReports) {
        Map<String, Map<String, List<Long>>> groupedData = activityReports.stream().collect(
                Collectors.groupingBy(activityReport -> activityReport.getClub().getName(),
                        Collectors.groupingBy(ActivityReport::getTerm,
                                Collectors.mapping(ActivityReport::getId, Collectors.toList()))));

        return groupedData.entrySet().stream().flatMap(entry -> {
            String clubName = entry.getKey();
            Map<String, List<Long>> termMap = entry.getValue();

            return termMap.entrySet().stream().map(termEntry -> {
                String term = termEntry.getKey();
                List<ActivityReportDto> activityReportDtos = termEntry.getValue().stream().map(ActivityReportDto::new)
                        .collect(Collectors.toList());
                return AllActivityReportResponse.of(clubName, term, activityReportDtos);
            });

        }).collect(Collectors.toList());
    }
}