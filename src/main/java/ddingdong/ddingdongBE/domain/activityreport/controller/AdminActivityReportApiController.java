package ddingdong.ddingdongBE.domain.activityreport.controller;

import ddingdong.ddingdongBE.domain.activityreport.api.AdminActivityReportApi;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.request.CreateActivityTermInfoRequest;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.AdminActivityReportListResponse;
import ddingdong.ddingdongBE.domain.activityreport.service.FacadeAdminActivityReportService;
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
    public void createActivityTermInfo(CreateActivityTermInfoRequest request) {
        facadeAdminActivityReportService.createActivityTermInfo(request.toCommand());
    }
}
