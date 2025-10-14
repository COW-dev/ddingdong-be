package ddingdong.ddingdongBE.domain.fixzone.service;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.query.AdminFixZoneListQuery;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.query.AdminFixZoneQuery;
import ddingdong.ddingdongBE.file.service.S3FileService;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlAndNameWithOrderQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlWithOrderQuery;
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
        List<UploadedFileUrlAndNameWithOrderQuery> imageUrlQueries = fileMetaDataService
                .getCoupledAllByDomainTypeAndEntityId(DomainType.FIX_ZONE_IMAGE, fixZoneId)
                .stream()
                .map(fileMetaData -> UploadedFileUrlAndNameWithOrderQuery.of(
                        s3FileService.getUploadedFileUrlAndName(fileMetaData.getFileKey(), fileMetaData.getFileName()), fileMetaData.getOrder()))
                .toList();
        Club club = fixZone.getClub();
        UploadedFileUrlQuery clubProfileImageQuery = fileMetaDataService
                .getCoupledAllByDomainTypeAndEntityId(DomainType.CLUB_PROFILE, club.getId())
                .stream()
                .findFirst()
                .map(fileMetaData -> s3FileService.getUploadedFileUrl(fileMetaData.getFileKey()))
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
