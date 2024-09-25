package ddingdong.ddingdongBE.domain.activityreport.controller;

import ddingdong.ddingdongBE.domain.activityreport.api.AdminActivityReportApi;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.request.CreateActivityTermInfoRequest;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportListResponse;
import ddingdong.ddingdongBE.domain.activityreport.service.FacadeActivityReportService;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportListQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminActivityReportApiController implements AdminActivityReportApi {

    private final FacadeActivityReportService facadeActivityReportService;

    @Override
    public List<ActivityReportListResponse> getActivityReports() {
        List<ActivityReportListQuery> queries = facadeActivityReportService.getActivityReports();
        return queries.stream()
            .map(ActivityReportListResponse::from)
            .toList();
    }

    @Override
    public void createActivityTermInfo(CreateActivityTermInfoRequest request) {
        facadeActivityReportService.createActivityTermInfo(request.toCommand());
    }
}
