package ddingdong.ddingdongBE.domain.activityreport.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.activityreport.api.ClubActivityReportApi;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.request.CreateActivityReportRequest;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.request.UpdateActivityReportRequest;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportDto;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportListResponse;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportResponse;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportTermInfoResponse;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.CurrentTermResponse;
import ddingdong.ddingdongBE.domain.activityreport.service.FacadeActivityReportService;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ClubActivityReportApiController implements ClubActivityReportApi {

    private final FacadeActivityReportService facadeActivityReportService;

    @Override
    public CurrentTermResponse getCurrentTerm() {
        return facadeActivityReportService.getCurrentTerm();
    }

    @Override
    public List<ActivityReportListResponse> getMyActivityReports(PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        return facadeActivityReportService.getMyActivityReports(user);
    }

    @Override
    public List<ActivityReportResponse> getActivityReport(
        String term,
        String clubName
    ) {
        return facadeActivityReportService.getActivityReport(term, clubName);
    }

    @Override
    public void createActivityReport(
        PrincipalDetails principalDetails,
        List<CreateActivityReportRequest> requests,
        MultipartFile firstImage,
        MultipartFile secondImage
    ) {
        User user = principalDetails.getUser();
        facadeActivityReportService.create(user, requests);
        List<ActivityReportDto> activityReportDtos = facadeActivityReportService.getActivityReportDtos(user, requests);
        facadeActivityReportService.uploadImages(activityReportDtos, firstImage, secondImage);
    }

    @Override
    public void updateActivityReport(
        PrincipalDetails principalDetails,
        String term,
        List<UpdateActivityReportRequest> requests,
        MultipartFile firstImage,
        MultipartFile secondImage
    ) {
        User user = principalDetails.getUser();
        facadeActivityReportService.update(user, term, requests);
        List<ActivityReportDto> activityReportDtos = facadeActivityReportService.getActivityReportDtos(user, term);
        facadeActivityReportService.updateImages(activityReportDtos, firstImage, secondImage);
    }

    @Override
    public void deleteActivityReport(
        PrincipalDetails principalDetails,
        String term
    ) {
        User user = principalDetails.getUser();
        List<ActivityReportDto> activityReportDtos = facadeActivityReportService.getActivityReportDtos(user, term);
        facadeActivityReportService.deleteImages(activityReportDtos);
        facadeActivityReportService.delete(user, term);
    }

    @Override
    public List<ActivityReportTermInfoResponse> getActivityTermInfos() {
        return facadeActivityReportService.getActivityReportTermInfos();
    }

}
