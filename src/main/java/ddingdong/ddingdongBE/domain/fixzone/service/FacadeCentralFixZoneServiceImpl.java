package ddingdong.ddingdongBE.domain.fixzone.service;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.command.CreateFixZoneCommand;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.command.UpdateFixZoneCommand;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.query.CentralFixZoneQuery;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.query.CentralMyFixZoneListQuery;
import ddingdong.ddingdongBE.file.service.S3FileService;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
        fileMetaDataService.updateAll(
                Objects.requireNonNullElse(command.fixZoneImageIds(), Collections.emptyList()),
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
        List<UploadedFileUrlQuery> imageUrlQueries = fileMetaDataService
                .getCoupledAllByEntityTypeAndEntityId(DomainType.FIX_ZONE_IMAGE, fixZoneId)
                .stream()
                .map(fileMetaData -> s3FileService.getUploadedFileUrl(fileMetaData.getFileKey()))
                .toList();

        UploadedFileUrlQuery clubProfileImageKey = fileMetaDataService
                .getCoupledAllByEntityTypeAndEntityId(DomainType.CLUB_PROFILE, club.getId())
                .stream()
                .map(fileMetaData -> s3FileService.getUploadedFileUrl(fileMetaData.getFileKey()))
                .findFirst()
                .orElse(null);
        return CentralFixZoneQuery.of(fixZone, imageUrlQueries, clubProfileImageKey);
    }

    @Override
    @Transactional
    public Long update(UpdateFixZoneCommand command) {
        FixZone fixZone = fixZoneService.getById(command.fixZoneId());
        fixZone.update(command.toEntity());
        fileMetaDataService.updateAll(
                Objects.requireNonNullElse(command.fixZoneImageIds(), Collections.emptyList()),
                DomainType.FIX_ZONE_IMAGE,
                fixZone.getId());
        return fixZone.getId();
    }

    @Override
    @Transactional
    public void delete(Long fixZoneId) {
        fixZoneService.delete(fixZoneId);
        fileMetaDataService.getCoupledAllByEntityTypeAndEntityId(DomainType.FIX_ZONE_IMAGE, fixZoneId)
                .forEach(fileMetaData -> fileMetaData.updateStatus(FileStatus.DELETED));
    }

}
