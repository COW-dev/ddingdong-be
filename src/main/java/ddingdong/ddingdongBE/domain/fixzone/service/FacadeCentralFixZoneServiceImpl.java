package ddingdong.ddingdongBE.domain.fixzone.service;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.domain.filemetadata.service.dto.FileMetaDataIdOrderDto;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.command.CreateFixZoneCommand;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.command.UpdateFixZoneCommand;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.query.CentralFixZoneQuery;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.query.CentralMyFixZoneListQuery;
import ddingdong.ddingdongBE.file.service.S3FileService;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlAndNameQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlAndNameWithOrderQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacadeCentralFixZoneServiceImpl implements FacadeCentralFixZoneService {

    private final FixZoneService fixZoneService;
    private final ClubService clubService;
    private final FileMetaDataService fileMetaDataService;
    private final S3FileService s3FileService;

    @Override
    @Transactional
    public Long create(CreateFixZoneCommand command) {
        Club club = clubService.getByUserId(command.userId());
        FixZone createdFixZone = command.toEntity(club);
        Long createdFixZoneId = fixZoneService.save(createdFixZone);

        List<FileMetaDataIdOrderDto> imageFileMetaDataIdOrderDtos = command.imageInfos().stream()
                .map(imageInfo -> FileMetaDataIdOrderDto.of(imageInfo.imageId(), imageInfo.order()))
                .toList();

        fileMetaDataService.updateStatusToCoupledWithOrder(
                imageFileMetaDataIdOrderDtos,
                DomainType.FIX_ZONE_IMAGE,
                createdFixZoneId
        );
        return createdFixZoneId;
    }

    @Override
    public List<CentralMyFixZoneListQuery> getMyFixZones(Long userId) {
        Club club = clubService.getByUserId(userId);
        return fixZoneService.findAllByClubId(club.getId())
                .stream()
                .map(CentralMyFixZoneListQuery::from)
                .toList();
    }

    @Override
    public CentralFixZoneQuery getFixZone(Long fixZoneId) {
        FixZone fixZone = fixZoneService.getById(fixZoneId);
        Club club = fixZone.getClub();
        List<UploadedFileUrlAndNameWithOrderQuery> imageUrlQueries = fileMetaDataService
                .getCoupledAllByDomainTypeAndEntityId(DomainType.FIX_ZONE_IMAGE, fixZoneId)
                .stream()
                .map(fileMetaData -> UploadedFileUrlAndNameWithOrderQuery.of(
                        s3FileService.getUploadedFileUrlAndName(fileMetaData.getFileKey(), fileMetaData.getFileName()), fileMetaData.getOrder()))
                .toList();

        UploadedFileUrlAndNameQuery clubProfileImageKey = fileMetaDataService
                .getCoupledAllByDomainTypeAndEntityId(DomainType.CLUB_PROFILE, club.getId())
                .stream()
                .map(fileMetaData -> s3FileService.getUploadedFileUrlAndName(fileMetaData.getFileKey(), fileMetaData.getFileName()))
                .findFirst()
                .orElse(null);
        return CentralFixZoneQuery.of(fixZone, imageUrlQueries, clubProfileImageKey);
    }

    @Override
    @Transactional
    public Long update(UpdateFixZoneCommand command) {
        FixZone fixZone = fixZoneService.getById(command.fixZoneId());
        fixZone.update(command.toEntity());
        List<FileMetaDataIdOrderDto> imageFileMetaDataIdOrderDtos = command.imageInfos().stream()
                .map(imageInfo -> FileMetaDataIdOrderDto.of(imageInfo.imageId(), imageInfo.order()))
                .toList();
        fileMetaDataService.updateWithOrder(imageFileMetaDataIdOrderDtos, DomainType.FIX_ZONE_IMAGE, fixZone.getId());
        return fixZone.getId();
    }

    @Override
    @Transactional
    public void delete(Long fixZoneId) {
        fixZoneService.delete(fixZoneId);
        fileMetaDataService.updateStatusToDelete(DomainType.FIX_ZONE_IMAGE, fixZoneId);
    }
}
