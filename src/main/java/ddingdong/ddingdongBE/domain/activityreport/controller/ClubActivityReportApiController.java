package ddingdong.ddingdongBE.domain.activityreport.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.activityreport.api.ClubActivityReportApi;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.request.CreateActivityReportRequest;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.request.UpdateActivityReportRequest;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportListResponse;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportResponse;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportTermInfoResponse;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.CurrentTermResponse;
import ddingdong.ddingdongBE.domain.activityreport.service.FacadeClubActivityReportService;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.command.CreateActivityReportCommand;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.command.UpdateActivityReportCommand;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportListQuery;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportQuery;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportTermInfoQuery;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ClubActivityReportApiController implements ClubActivityReportApi {

    private final FacadeClubActivityReportService facadeClubActivityReportService;

    @Override
    public CurrentTermResponse getCurrentTerm() {
        String currentTerm = facadeClubActivityReportService.getCurrentTerm();
        return CurrentTermResponse.from(currentTerm);
    }

    @Override
    public List<ActivityReportListResponse> getMyActivityReports(PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        List<ActivityReportListQuery> queries = facadeClubActivityReportService.getMyActivityReports(
            user);
        return queries.stream()
            .map(ActivityReportListResponse::from)
            .toList();
    }

    @Override
    public List<ActivityReportResponse> getActivityReport(
        String term,
        String clubName
    ) {
        List<ActivityReportQuery> queries = facadeClubActivityReportService.getActivityReport(term,
            clubName);
        return queries.stream()
            .map(ActivityReportResponse::from)
            .toList();
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
        facadeClubActivityReportService.create(user, commands, Arrays.asList(firstImage, secondImage));
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
        facadeClubActivityReportService.update(user, term, commands, Arrays.asList(firstImage, secondImage));
    }

    @Override
    public void deleteActivityReport(
        PrincipalDetails principalDetails,
        String term
    ) {
        User user = principalDetails.getUser();
        facadeClubActivityReportService.delete(user, term);
    }

    @Override
    public List<ActivityReportTermInfoResponse> getActivityTermInfos() {
        List<ActivityReportTermInfoQuery> queries = facadeClubActivityReportService.getActivityReportTermInfos();
        return queries.stream()
            .map(ActivityReportTermInfoResponse::from)
            .toList();
    }
}
