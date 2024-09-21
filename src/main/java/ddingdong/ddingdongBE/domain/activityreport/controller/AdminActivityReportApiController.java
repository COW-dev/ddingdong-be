package ddingdong.ddingdongBE.domain.activityreport.controller;

import ddingdong.ddingdongBE.domain.activityreport.api.AdminActivityReportApi;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.request.CreateActivityTermInfoRequest;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportListResponse;
import ddingdong.ddingdongBE.domain.activityreport.service.AcitivtyReportFacadeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminActivityReportApiController implements AdminActivityReportApi {

    private final AcitivtyReportFacadeService acitivtyReportFacadeService;

    @Override
    public List<ActivityReportListResponse> getActivityReports() {
        return acitivtyReportFacadeService.getActivityReports();
    }

    @Override
    public void createActivityTermInfo(CreateActivityTermInfoRequest request) {
        acitivtyReportFacadeService.createActivityTermInfo(request.toCommand());
    }
}
