package ddingdong.ddingdongBE.domain.activityreport.controller;

import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportResponse;
import ddingdong.ddingdongBE.domain.activityreport.service.ActivityReportService;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin/activity-reports")
public class AdminActivityReportApiController {

    private final ActivityReportService activityReportService;

    @GetMapping
    public List<ActivityReportResponse> getActivityReports() {
        return activityReportService.getAll();
    }
}
