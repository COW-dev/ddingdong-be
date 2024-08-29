package ddingdong.ddingdongBE.domain.activityreport.controller;

import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.ACTIVITY_REPORT;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.IMAGE;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.activityreport.api.ClubActivityReportApi;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.request.CreateActivityReportRequest;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.request.UpdateActivityReportRequest;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportDto;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportListResponse;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportResponse;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.CurrentTermResponse;
import ddingdong.ddingdongBE.domain.activityreport.service.ActivityReportService;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.file.service.FileService;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ClubActivityReportApiController implements ClubActivityReportApi {

    private final ActivityReportService activityReportService;
    private final FileService fileService;

    public CurrentTermResponse getCurrentTerm() {
        return activityReportService.getCurrentTerm();
    }

    public List<ActivityReportListResponse> getMyActivityReports(PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        return activityReportService.getMyActivityReports(user);
    }

    public List<ActivityReportResponse> getActivityReport(
        String term,
        String clubName
    ) {
        return activityReportService.getActivityReport(term, clubName);
    }

    public void createActivityReport(
        PrincipalDetails principalDetails,
        List<CreateActivityReportRequest> requests,
        MultipartFile firstImage,
        MultipartFile secondImage
    ) {
        User user = principalDetails.getUser();

        List<MultipartFile> images = List.of(firstImage, secondImage);

        IntStream.range(0, requests.size())
            .forEach(index -> {
                CreateActivityReportRequest request = requests.get(index);
                Long registeredActivityReportId = activityReportService.create(user, request);

                if (index < images.size() && images.get(index) != null && !images.get(index).isEmpty()) {
                    fileService.uploadFile(
                        registeredActivityReportId,
                        Collections.singletonList(images.get(index)),
                        IMAGE,
                        ACTIVITY_REPORT
                    );
                }
            });

    }

    public void updateActivityReport(
        PrincipalDetails principalDetails,
        String term,
        List<UpdateActivityReportRequest> requests,
        MultipartFile firstImage,
        MultipartFile secondImage
    ) {
        User user = principalDetails.getUser();

        List<ActivityReportDto> activityReportDtos = activityReportService.update(user, term, requests);
        List<MultipartFile> images = List.of(firstImage, secondImage);

        IntStream.range(0, Math.min(activityReportDtos.size(), images.size()))
            .filter(index -> images.get(index) != null && !images.get(index).isEmpty())
            .forEach(index -> {
                    fileService.deleteFile(
                        activityReportDtos.get(index).getId(),
                        IMAGE,
                        ACTIVITY_REPORT
                    );

                    fileService.uploadFile(
                        activityReportDtos.get(index).getId(),
                        Collections.singletonList(images.get(index)),
                        IMAGE,
                        ACTIVITY_REPORT
                    );
                }
            );
    }

    public void deleteActivityReport(
        PrincipalDetails principalDetails,
        String term
    ) {
        User user = principalDetails.getUser();

        activityReportService.delete(user, term)
            .forEach(it -> fileService.deleteFile(it.getId(), IMAGE, ACTIVITY_REPORT));
    }
}
