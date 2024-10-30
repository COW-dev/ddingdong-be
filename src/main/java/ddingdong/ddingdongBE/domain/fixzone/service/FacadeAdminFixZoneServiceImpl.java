package ddingdong.ddingdongBE.domain.fixzone.service;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.query.AdminFixZoneListQuery;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.query.AdminFixZoneQuery;
import ddingdong.ddingdongBE.file.service.S3FileService;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacadeAdminFixZoneServiceImpl implements FacadeAdminFixZoneService {

    private final FixZoneService fixZoneService;
    private final FileMetaDataService fileMetaDataService;
    private final S3FileService s3FileService;

    @Override
    public List<AdminFixZoneListQuery> getAll() {
        return fixZoneService.findAll().stream()
                .map(AdminFixZoneListQuery::from)
                .toList();
    }

    @Override
    public AdminFixZoneQuery getFixZone(Long fixZoneId) {
        FixZone fixZone = fixZoneService.getById(fixZoneId);
        List<UploadedFileUrlQuery> imageUrlQueries = fileMetaDataService
                .getCoupledAllByEntityTypeAndEntityId(DomainType.FIZ_ZONE_IMAGE, fixZoneId)
                .stream()
                .map(FileMetaData::getFileKey)
                .map(s3FileService::getUploadedFileUrl)
                .toList();
        Club club = fixZone.getClub();
        UploadedFileUrlQuery clubProfileImageQuery = fileMetaDataService
                .getCoupledAllByEntityTypeAndEntityId(DomainType.CLUB_PROFILE, club.getId())
                .stream()
                .findFirst()
                .map(FileMetaData::getFileKey)
                .map(s3FileService::getUploadedFileUrl)
                .orElse(null);
        return AdminFixZoneQuery.of(fixZone, imageUrlQueries, clubProfileImageQuery);
    }

    @Override
    @Transactional
    public void updateToComplete(Long fixZoneId) {
        FixZone fixZone = fixZoneService.getById(fixZoneId);
        fixZone.updateToComplete();
    }

    @Override
    @Transactional
    public void delete(Long fixZoneId) {
        fixZoneService.delete(fixZoneId);
    }
}
