package ddingdong.ddingdongBE.domain.activityreport.controller;

import ddingdong.ddingdongBE.domain.activityreport.api.AdminActivityReportApi;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.request.CreateActivityTermInfoRequest;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.request.UpdateActivityReportRequest;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.request.UpdateActivityReportRequests;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportResponse;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.AdminActivityReportListResponse;
import ddingdong.ddingdongBE.domain.activityreport.service.FacadeAdminActivityReportService;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.command.UpdateActivityReportCommand;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportQuery;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.AdminActivityReportListQuery;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminActivityReportApiController implements AdminActivityReportApi {

    private final FacadeAdminActivityReportService facadeAdminActivityReportService;

    @Override
    public List<AdminActivityReportListResponse> getActivityReports(int term) {
        LocalDateTime now = LocalDateTime.now();
        List<AdminActivityReportListQuery> queries = facadeAdminActivityReportService.getActivityReports(now, term);
        return queries.stream()
                .map(AdminActivityReportListResponse::from)
                .toList();
    }

    @Override
    public List<ActivityReportResponse> getActivityReport(Long clubId, int term) {
        LocalDateTime now = LocalDateTime.now();
        List<ActivityReportQuery> queries = facadeAdminActivityReportService.getActivityReport(clubId, now, term);
        return queries.stream()
                .map(ActivityReportResponse::from)
                .toList();
    }

    @Override
    public void createActivityTermInfo(CreateActivityTermInfoRequest request) {
        facadeAdminActivityReportService.createActivityTermInfo(request.toCommand());
    }

    @Override
    public void deleteActivityTermInfo(int term) {
        LocalDateTime now = LocalDateTime.now();
        facadeAdminActivityReportService.deleteActivityTermInfo(now, term);
    }

    @Override
    public void updateActivityReport(Long clubId, int term, UpdateActivityReportRequests requests) {
        LocalDateTime now = LocalDateTime.now();
        List<UpdateActivityReportCommand> commands = requests.activityReportRequests().stream()
                .map(UpdateActivityReportRequest::toCommand)
                .toList();
        facadeAdminActivityReportService.updateActivityReport(clubId, now, term, commands);
    }

    @Override
    public void deleteActivityReport(Long clubId, int term) {
        LocalDateTime now = LocalDateTime.now();
        facadeAdminActivityReportService.deleteActivityReport(clubId, now, term);
    }
}
