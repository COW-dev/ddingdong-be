package ddingdong.ddingdongBE.domain.activityreport.controller;

import static ddingdong.ddingdongBE.domain.imageinformation.entity.ImageCategory.ACTIVITY_REPORT;

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
@RequestMapping("api/club")
public class ClubActivityReportApiController {

    private static final int IMAGE_COUNT = 2;

    private final ActivityReportService activityReportService;
    private final FileService fileService;

    @GetMapping("activity-reports/current-term")
    public CurrentTermResponse getCurrentTerm() {
        return activityReportService.getCurrentTerm();
    }

    @GetMapping("/my/activity-reports")
    public List<ActivityReportResponse> getMyActivityReports(
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
            @RequestPart("reportData") List<RegisterActivityReportRequest> requests,
            @RequestPart("uploadFiles") List<MultipartFile> images
    ) {
        validateImages(images);

        User user = principalDetails.getUser();

        IntStream.range(0, requests.size())
                .forEach(index -> {
                    RegisterActivityReportRequest request = requests.get(index);
                    MultipartFile image = images.get(index);

                    Long registeredActivityReportId = activityReportService.register(user, request);
                    fileService.uploadImageFile(registeredActivityReportId, Collections.singletonList(image),
                            ACTIVITY_REPORT);
                });
    }

    @PatchMapping("my/activity-reports")
    public void updateReport (
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam("term") String term,
            @RequestPart("reportData") List<UpdateActivityReportRequest> requests,
            @RequestPart(name = "uploadFiles") List<MultipartFile> images
    ) {
        User user = principalDetails.getUser();

        List<UpdateActivityReportResponse> responses = activityReportService.update(user, term, requests);

        IntStream.range(0, responses.size())
            .forEach(index -> {
                    fileService.deleteImageFile(responses.get(index).getId(), ACTIVITY_REPORT);
                    fileService.uploadImageFile(responses.get(index).getId(), Collections.singletonList(images.get(index)), ACTIVITY_REPORT);
                }
            );
    }

    @DeleteMapping("my/activity-reports")
    public void deleteReport (
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestParam("term") String term
    ) {
        User user = principalDetails.getUser();
        List<ActivityReportDto> deleteActivityReportDtos = activityReportService.delete(user, term);

        deleteActivityReportDtos
                .forEach(
                        activityReportDto -> fileService.deleteImageFile(activityReportDto.getId(), ACTIVITY_REPORT)
                );
    }

    private void validateImages(List<MultipartFile> images) {
        if (images.size() != IMAGE_COUNT) {
            throw new IllegalArgumentException("업로드한 보고서 수와 이미지 수가 일치하지 않습니다.");
        }
    }
}
