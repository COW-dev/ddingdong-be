package ddingdong.ddingdongBE.domain.vodprocessing.controller;

import ddingdong.ddingdongBE.domain.vodprocessing.controller.dto.request.CreatePendingVodProcessingJobRequest;
import ddingdong.ddingdongBE.domain.vodprocessing.controller.dto.request.UpdateVodProcessingJobStatusRequest;
import ddingdong.ddingdongBE.domain.vodprocessing.service.FacadeVodProcessingJobService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/server/internal/trigger/vod-processing-job")
@RequiredArgsConstructor
public class VodProcessingJobController {

    private final FacadeVodProcessingJobService facadeVodProcessingJobService;

    @PostMapping()
    public void createPending(@RequestBody CreatePendingVodProcessingJobRequest request) {
        facadeVodProcessingJobService.create(request.toCommand());
    }

    @PatchMapping("/job-status")
    public void updateJobStatus(@RequestBody @Valid UpdateVodProcessingJobStatusRequest request) {
        facadeVodProcessingJobService.updateVodProcessingJobStatus(request.toCommand());
    }

}
