package ddingdong.ddingdongBE.domain.qrstamp.controller;

import ddingdong.ddingdongBE.domain.qrstamp.controller.dto.request.CollectStampRequest;
import ddingdong.ddingdongBE.domain.qrstamp.controller.dto.response.CollectionResultResponse;
import ddingdong.ddingdongBE.domain.qrstamp.service.QrStampService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/server/qr-stamps")
@RequiredArgsConstructor
public class QrStampController {

    private final QrStampService qrStampService;

    @PostMapping("/collect")
    public void collectStamp(@RequestBody CollectStampRequest request) {
        LocalDateTime collectedAt = LocalDateTime.now();
        qrStampService.collectStamp(request, collectedAt);
    }

    @GetMapping()
    public CollectionResultResponse getCollectionResult(@RequestParam String studentName, @RequestParam String studentNumber) {
        return qrStampService.getCollectionResult(studentName, studentNumber);
    }

}
