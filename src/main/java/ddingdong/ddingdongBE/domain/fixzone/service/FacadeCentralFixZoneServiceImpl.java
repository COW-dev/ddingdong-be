package ddingdong.ddingdongBE.domain.fixzone.service;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.service.FacadeFileMetaDataService;
import ddingdong.ddingdongBE.domain.filemetadata.service.dto.command.UpdateAllFileMetaDataCommand;
import ddingdong.ddingdongBE.domain.filemetadata.service.dto.query.FileMetaDataListQuery;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.command.CreateFixZoneCommand;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.command.UpdateFixZoneCommand;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.query.CentralFixZoneQuery;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.query.CentralMyFixZoneListQuery;
import ddingdong.ddingdongBE.file.service.S3FileService;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacadeCentralFixZoneServiceImpl implements FacadeCentralFixZoneService {

    private final FixZoneService fixZoneService;
    private final ClubService clubService;
    private final FacadeFileMetaDataService facadeFileMetaDataService;
    private final S3FileService s3FileService;

    @Override
    @Transactional
    public Long create(CreateFixZoneCommand command) {
        Club club = clubService.getByUserId(command.userId());
        FixZone createdFixZone = command.toEntity(club);
        Long createdFixZoneId = fixZoneService.save(createdFixZone);
        for (String fixZoneImageId : command.fixZoneImageIds()) {
            updateFileMetaData(fixZoneImageId, createdFixZoneId);
        }
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
        List<UploadedFileUrlQuery> imageUrlQueries = facadeFileMetaDataService
                .getAllByEntityTypeAndEntityId(DomainType.FIZ_ZONE_IMAGE, fixZoneId)
                .stream()
                .map(FileMetaDataListQuery::key)
                .map(s3FileService::getUploadedFileUrl)
                .toList();

        UploadedFileUrlQuery clubProfileImageKey = facadeFileMetaDataService
                .getAllByEntityTypeAndEntityId(DomainType.CLUB_PROFILE, club.getId())
                .stream()
                .map(FileMetaDataListQuery::key)
                .map(s3FileService::getUploadedFileUrl)
                .findFirst()
                .orElse(null);
        return CentralFixZoneQuery.of(fixZone, imageUrlQueries, clubProfileImageKey);
    }

    @Override
    @Transactional
    public Long update(UpdateFixZoneCommand command) {
        FixZone fixZone = fixZoneService.getById(command.fixZoneId());
        fixZone.update(command.toEntity());
        return fixZone.getId();
    }

    @Override
    @Transactional
    public void delete(Long fixZoneId) {
        fixZoneService.delete(fixZoneId);
    }

}
