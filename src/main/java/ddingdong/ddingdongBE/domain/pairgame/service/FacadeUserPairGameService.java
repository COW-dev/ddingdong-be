package ddingdong.ddingdongBE.domain.pairgame.service;

import ddingdong.ddingdongBE.domain.pairgame.service.dto.command.CreatePairGameApplierCommand;
import ddingdong.ddingdongBE.domain.pairgame.service.dto.query.PairGameApplierAmountQuery;
import ddingdong.ddingdongBE.file.service.S3FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacadeUserPairGameService {

    private final PairGameService pairGameService;
    private final S3FileService s3FileService;

    @Transactional
    public void createApplier(CreatePairGameApplierCommand createPairGameApplierCommand, MultipartFile studentFeeImageFile) {
        String key = s3FileService.uploadMultipartFile(studentFeeImageFile, LocalDateTime.now(), "pair-game");
        String studentFeeImageUrl = s3FileService.getUploadedMultipartFileUrl(key);
        pairGameService.create(createPairGameApplierCommand.toEntity(studentFeeImageUrl));
    }

    public PairGameApplierAmountQuery getPairGameApplierAmount() {
        return pairGameService.getPairGameApplierAmount();
    }
}
