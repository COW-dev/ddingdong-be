package ddingdong.ddingdongBE.domain.pairgame.service;

import ddingdong.ddingdongBE.common.exception.FileException.UploadedFileNotFoundException;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.domain.pairgame.service.dto.command.CreatePairGameApplierCommand;
import ddingdong.ddingdongBE.domain.pairgame.service.dto.query.PairGameApplierAmountQuery;
import ddingdong.ddingdongBE.domain.pairgame.service.dto.query.PairGameMetaDataQuery;
import ddingdong.ddingdongBE.file.service.S3FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacadeUserPairGameService {

    private final PairGameService pairGameService;
    private final S3FileService s3FileService;
    private final FileMetaDataService fileMetaDataService;

    @Transactional
    public void createApplier(CreatePairGameApplierCommand createPairGameApplierCommand, MultipartFile studentFeeImageFile) {
        if (studentFeeImageFile == null || studentFeeImageFile.isEmpty()) {
            throw new UploadedFileNotFoundException();
        }
        String key = s3FileService.uploadMultipartFile(studentFeeImageFile, LocalDateTime.now(), "pair-game");
        String studentFeeImageUrl = s3FileService.getUploadedFileUrl(key).cdnUrl();
        pairGameService.create(createPairGameApplierCommand.toEntity(studentFeeImageUrl));
    }

    public PairGameApplierAmountQuery getPairGameApplierAmount() {
        return pairGameService.getPairGameApplierAmount();
    }

    public PairGameMetaDataQuery getPairGameMetaData() {
        List<FileMetaData> allClubProfileMetaData = fileMetaDataService.getCoupledAllByDomainType(DomainType.CLUB_PROFILE);
        Collections.shuffle(allClubProfileMetaData);
        List<String> pairGameMetaData = allClubProfileMetaData.stream()
                .limit(18)
                .map(file -> s3FileService.getUploadedFileUrl(file.getFileKey()).cdnUrl())
                .collect(Collectors.toList());
        return PairGameMetaDataQuery.of(pairGameMetaData);
    }
}
