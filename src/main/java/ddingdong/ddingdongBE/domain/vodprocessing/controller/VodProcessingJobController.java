package ddingdong.ddingdongBE.domain.vodprocessing.controller;

import ddingdong.ddingdongBE.domain.vodprocessing.controller.dto.request.CreatePendingVodProcessingJobRequest;
import ddingdong.ddingdongBE.domain.vodprocessing.service.FacadeVodProcessingJobService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController("/server/internal/trigger/vod-processing-job")
@RequiredArgsConstructor
public class VodProcessingJobController {

    private final FacadeVodProcessingJobService facadeVodProcessingJobService;

    @PostMapping()
    public void createPending(CreatePendingVodProcessingJobRequest request) {
        facadeVodProcessingJobService.create(request.toCommand());
    }

}
