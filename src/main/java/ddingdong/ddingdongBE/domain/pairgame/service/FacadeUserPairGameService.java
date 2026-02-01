package ddingdong.ddingdongBE.domain.pairgame.service;

import ddingdong.ddingdongBE.common.exception.FileException.UploadedFileNotFoundException;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.domain.pairgame.service.dto.command.CreatePairGameApplierCommand;
import ddingdong.ddingdongBE.domain.pairgame.service.dto.query.PairGameApplierAmountQuery;
import ddingdong.ddingdongBE.domain.pairgame.service.dto.query.PairGameMetaDataQuery;
import ddingdong.ddingdongBE.domain.pairgame.service.dto.query.PairGameMetaDataQuery.PairGameClubAndImageQuery;
import ddingdong.ddingdongBE.file.service.S3FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacadeUserPairGameService {

    private final PairGameService pairGameService;
    private final S3FileService s3FileService;
    private final FileMetaDataService fileMetaDataService;
    private final ClubService clubService;

    @Transactional
    public void createPairGameApplier(CreatePairGameApplierCommand createPairGameApplierCommand) {
        MultipartFile studentFeeImageFile = createPairGameApplierCommand.studentFeeImageFile();
        if (studentFeeImageFile == null || studentFeeImageFile.isEmpty()) {
            throw new UploadedFileNotFoundException();
        }
        pairGameService.validateStudentNumberUnique(createPairGameApplierCommand.studentNumber());
        String key = s3FileService.uploadMultipartFile(studentFeeImageFile, LocalDateTime.now(), "pair-game");
        pairGameService.create(createPairGameApplierCommand.toEntity(key));
    }

    public PairGameApplierAmountQuery getPairGameApplierAmount() {
        return pairGameService.getPairGameApplierAmount();
    }

    public PairGameMetaDataQuery getPairGameMetaData() {
        List<FileMetaData> allClubProfileMetaData = fileMetaDataService.getCoupledAllByDomainType(DomainType.CLUB_PROFILE);
        Collections.shuffle(allClubProfileMetaData);
        List<FileMetaData> selectedMetaData = allClubProfileMetaData.stream().limit(18).toList();

        List<Long> clubIds = selectedMetaData.stream().map(FileMetaData::getEntityId).toList();
        List<Club> clubs = clubService.getAllByIds(clubIds);
        Map<Long, String> clubNameMap = clubs.stream().collect(Collectors.toMap(Club::getId, Club::getName));

        List<PairGameClubAndImageQuery> pairGameMetaData = selectedMetaData.stream().map(file -> {
            String name = clubNameMap.get(file.getEntityId());
            String imageUrl = s3FileService.getUploadedFileUrl(file.getFileKey()).cdnUrl();
            return PairGameClubAndImageQuery.builder().clubName(name).imageUrl(imageUrl).build();
        }).toList();

        return PairGameMetaDataQuery.builder().metaData(pairGameMetaData).build();
    }
}
