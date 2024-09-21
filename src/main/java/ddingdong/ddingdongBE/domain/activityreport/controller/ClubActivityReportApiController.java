package ddingdong.ddingdongBE.domain.activityreport.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.activityreport.api.ClubActivityReportApi;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.request.CreateActivityReportRequest;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.request.UpdateActivityReportRequest;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportListResponse;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportResponse;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportTermInfoResponse;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.CurrentTermResponse;
import ddingdong.ddingdongBE.domain.activityreport.service.FacadeActivityReportService;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.command.CreateActivityReportCommand;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.command.UpdateActivityReportCommand;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportQuery;
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
        List<CreateActivityReportCommand> commands = requests.stream()
            .map(CreateActivityReportRequest::toCommand)
            .toList();
        facadeActivityReportService.create(user, commands);
        List<ActivityReportQuery> activityReportQueries = facadeActivityReportService.getActivityReportInfos(user, commands);
        facadeActivityReportService.uploadImages(activityReportQueries, firstImage, secondImage);
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
        List<UpdateActivityReportCommand> commands = requests.stream()
            .map(UpdateActivityReportRequest::toCommand)
            .toList();
        facadeActivityReportService.update(user, term, commands);
        List<ActivityReportQuery> activityReportQueries = facadeActivityReportService.getActivityReportInfos(user, term);
        facadeActivityReportService.updateImages(activityReportQueries, firstImage, secondImage);
    }

    @Override
    public void deleteActivityReport(
        PrincipalDetails principalDetails,
        String term
    ) {
        User user = principalDetails.getUser();
        List<ActivityReportQuery> activityReportQueries = facadeActivityReportService.getActivityReportInfos(user, term);
        facadeActivityReportService.deleteImages(activityReportQueries);
        facadeActivityReportService.delete(user, term);
    }

    @Override
    public List<ActivityReportTermInfoResponse> getActivityTermInfos() {
        return facadeActivityReportService.getActivityReportTermInfos();
    }
}
