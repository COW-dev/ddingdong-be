package ddingdong.ddingdongBE.domain.activityreport.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.activityreport.api.ClubActivityReportApi;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.request.CreateActivityReportRequest;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.request.CreateActivityReportRequests;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.request.UpdateActivityReportRequest;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.request.UpdateActivityReportRequests;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.AdminActivityReportListResponse;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportResponse;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportTermInfoResponse;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.CentralActivityReportListResponse;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.CurrentTermResponse;
import ddingdong.ddingdongBE.domain.activityreport.service.FacadeClubActivityReportService;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.command.CreateActivityReportCommand;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.command.UpdateActivityReportCommand;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportQuery;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportTermInfoQuery;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.CentralActivityReportListQuery;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClubActivityReportApiController implements ClubActivityReportApi {

    private final FacadeClubActivityReportService facadeClubActivityReportService;

    @Override
    public CurrentTermResponse getCurrentTerm() {
        LocalDateTime now = LocalDateTime.now();
        int currentTerm = facadeClubActivityReportService.getCurrentTerm(now);
        return CurrentTermResponse.from(currentTerm);
    }

    @Override
    public List<CentralActivityReportListResponse> getMyActivityReports(PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        LocalDateTime now = LocalDateTime.now();
        List<CentralActivityReportListQuery> queries = facadeClubActivityReportService.getMyActivityReports(user, now);
        return queries.stream()
                .map(CentralActivityReportListResponse::from)
                .toList();
    }

    @Override
    public List<ActivityReportResponse> getActivityReport(PrincipalDetails principalDetails, int term) {
        User user = principalDetails.getUser();
        LocalDateTime now = LocalDateTime.now();
        List<ActivityReportQuery> queries = facadeClubActivityReportService.getActivityReport(user, now, term);
        return queries.stream()
                .map(ActivityReportResponse::from)
                .toList();
    }

    @Override
    public void createActivityReport(
            PrincipalDetails principalDetails,
            CreateActivityReportRequests requests
    ) {
        User user = principalDetails.getUser();
        List<CreateActivityReportCommand> commands = requests.activityReportRequests().stream()
                .map(CreateActivityReportRequest::toCommand)
                .toList();
        facadeClubActivityReportService.create(user, commands);
    }

    @Override
    public void updateActivityReport(
            PrincipalDetails principalDetails,
            int term,
            UpdateActivityReportRequests requests
    ) {
        User user = principalDetails.getUser();
        LocalDateTime now = LocalDateTime.now();
        List<UpdateActivityReportCommand> commands = requests.activityReportRequests().stream()
                .map(UpdateActivityReportRequest::toCommand)
                .toList();
        facadeClubActivityReportService.update(user, now, term, commands);
    }

    @Override
    public void deleteActivityReport(PrincipalDetails principalDetails, int term) {
        User user = principalDetails.getUser();
        LocalDateTime now = LocalDateTime.now();
        facadeClubActivityReportService.delete(user, now, term);
    }

    @Override
    public List<ActivityReportTermInfoResponse> getActivityTermInfos() {
        List<ActivityReportTermInfoQuery> queries = facadeClubActivityReportService.getActivityReportTermInfos();
        return queries.stream()
                .map(ActivityReportTermInfoResponse::from)
                .toList();
    }
}
