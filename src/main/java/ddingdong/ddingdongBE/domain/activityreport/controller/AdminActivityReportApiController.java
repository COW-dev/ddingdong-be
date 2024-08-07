package ddingdong.ddingdongBE.domain.activityreport.controller;

import ddingdong.ddingdongBE.domain.activityreport.api.AdminActivityReportApi;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.request.CreateActivityTermInfoRequest;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportTermInfoResponse;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.AllActivityReportResponse;
import ddingdong.ddingdongBE.domain.activityreport.service.ActivityReportService;
import ddingdong.ddingdongBE.domain.activityreport.service.ActivityReportTermInfoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminActivityReportApiController implements AdminActivityReportApi {

    private final ActivityReportService activityReportService;
    private final ActivityReportTermInfoService activityReportTermInfoService;

    @GetMapping
    public List<AllActivityReportResponse> getActivityReports() {
        return activityReportService.getAll();
    }

    @Override
    public List<ActivityReportTermInfoResponse> getActivityTermInfos() {
        return activityReportTermInfoService.getAll();
    }

    @Override
    public void createActivityTermInfo(CreateActivityTermInfoRequest request) {
        activityReportTermInfoService.create(request.startDate(), request.totalTermCount());
    }

}
