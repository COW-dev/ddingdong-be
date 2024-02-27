package ddingdong.ddingdongBE.domain.qrstamp.controller;

import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.EVENT;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.IMAGE;

import ddingdong.ddingdongBE.domain.event.controller.dto.request.ApplyEventRequest;
import ddingdong.ddingdongBE.domain.fileinformation.service.FileInformationService;
import ddingdong.ddingdongBE.domain.qrstamp.controller.dto.request.CollectStampRequest;
import ddingdong.ddingdongBE.domain.qrstamp.controller.dto.response.CollectionResultResponse;
import ddingdong.ddingdongBE.domain.qrstamp.service.QrStampService;
import ddingdong.ddingdongBE.file.service.FileService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/server/events")
@RequiredArgsConstructor
public class UserEventController {

    private final QrStampService qrStampService;
    private final FileService fileService;
    private final FileInformationService fileInformationService;

    @PostMapping("/stamps")
    public String collectStamp(@RequestBody CollectStampRequest request) {
        LocalDateTime collectedAt = LocalDateTime.now();
        return qrStampService.collectStamp(request, collectedAt);
    }

    @GetMapping("/stamps")
    public CollectionResultResponse getCollectionResult(@RequestParam String studentName, @RequestParam String studentNumber) {
        return qrStampService.getCollectionResult(studentName, studentNumber);
    }

    @PatchMapping("/apply")
    public void applyEvent(
        @ModelAttribute ApplyEventRequest request,
        @RequestPart(name = "certificationImage", required = false) MultipartFile image
    ) {
        Long stampHistoryId = qrStampService.findByStudentNumber(request.studentNumber());

        fileService.uploadFile(stampHistoryId, List.of(image), IMAGE, EVENT);

        List<String> imageUrls = fileInformationService.getImageUrls(IMAGE.getFileType() + EVENT.getFileDomain() + stampHistoryId);

        qrStampService.applyEvent(request, imageUrls);
    }

}
