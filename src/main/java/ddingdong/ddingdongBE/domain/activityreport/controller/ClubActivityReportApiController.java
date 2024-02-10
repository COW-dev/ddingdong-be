package ddingdong.ddingdongBE.domain.activityreport.controller;

import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.ACTIVITY_REPORT;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.IMAGE;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.request.RegisterActivityReportRequest;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.request.UpdateActivityReportRequest;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportDto;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.AllActivityReportResponse;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.CurrentTermResponse;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.DetailActivityReportResponse;
import ddingdong.ddingdongBE.domain.activityreport.service.ActivityReportService;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.file.service.FileService;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/server/club")
public class ClubActivityReportApiController {

    private final ActivityReportService activityReportService;
    private final FileService fileService;

    @GetMapping("activity-reports/current-term")
    public CurrentTermResponse getCurrentTerm() {
        return activityReportService.getCurrentTerm();
    }

    @GetMapping("/my/activity-reports")
    public List<AllActivityReportResponse> getMyActivityReports(
        @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        User user = principalDetails.getUser();
        return activityReportService.getMyActivityReports(user);
    }

    @GetMapping("/activity-reports")
    public List<DetailActivityReportResponse> getActivityReport(
        @RequestParam("term") String term,
        @RequestParam("club_name") String clubName
    ) {
        return activityReportService.getActivityReport(term, clubName);
    }

    @PostMapping("/my/activity-reports")
    public void registerReport(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestPart(value = "reportData", required = false) List<RegisterActivityReportRequest> requests,
        @RequestPart(value = "uploadFiles1", required = false) MultipartFile firstImage,
        @RequestPart(value = "uploadFiles2", required = false) MultipartFile secondImage
    ) {
        User user = principalDetails.getUser();

        IntStream.range(0, requests.size())
            .forEach(index -> {

                RegisterActivityReportRequest request = requests.get(index);
                Long registeredActivityReportId = activityReportService.register(user, request);

                if (index == 0 && !firstImage.isEmpty()) {
                    fileService.uploadFile(registeredActivityReportId,
                        Collections.singletonList(firstImage),
                        IMAGE, ACTIVITY_REPORT);
                }

                if (index == 1 && !secondImage.isEmpty()) {
                    fileService.uploadFile(registeredActivityReportId,
                        Collections.singletonList(secondImage),
                        IMAGE, ACTIVITY_REPORT);
                }
            });
    }

    @PatchMapping("my/activity-reports")
    public void updateReport(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestParam("term") String term,
        @RequestPart(value = "reportData", required = false) List<UpdateActivityReportRequest> requests,
        @RequestPart(value = "uploadFiles1", required = false) MultipartFile firstImage,
        @RequestPart(value = "uploadFiles2", required = false) MultipartFile secondImage
    ) {
        User user = principalDetails.getUser();

        List<ActivityReportDto> updateActivityReportDtos = activityReportService.update(user, term,
            requests);

        IntStream.range(0, updateActivityReportDtos.size())
            .forEach(index -> {
                    if (index == 0) {
                        fileService.deleteFile(updateActivityReportDtos.get(index).getId(), IMAGE,
                            ACTIVITY_REPORT);

                        if (!firstImage.isEmpty()) {
                            fileService.uploadFile(updateActivityReportDtos.get(index).getId(), Collections.singletonList(firstImage),
                                IMAGE,
                                ACTIVITY_REPORT);
                        }
                    }
                    if (index == 1) {
                        fileService.deleteFile(updateActivityReportDtos.get(index).getId(), IMAGE,
                            ACTIVITY_REPORT);

                        if (!secondImage.isEmpty()) {
                            fileService.uploadFile(updateActivityReportDtos.get(index).getId(), Collections.singletonList(secondImage),
                                IMAGE,
                                ACTIVITY_REPORT);
                        }
                    }
                }
            );
    }

    @DeleteMapping("my/activity-reports")
    public void deleteReport(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestParam("term") String term
    ) {
        User user = principalDetails.getUser();
        List<ActivityReportDto> deleteActivityReportDtos = activityReportService.delete(user, term);

        deleteActivityReportDtos
            .forEach(
                activityReportDto -> fileService.deleteFile(activityReportDto.getId(), IMAGE,
                    ACTIVITY_REPORT)
            );
    }
}
