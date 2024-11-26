package ddingdong.ddingdongBE.domain.banner.service;

import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import ddingdong.ddingdongBE.domain.banner.service.dto.command.CreateBannerCommand;
import ddingdong.ddingdongBE.domain.banner.service.dto.query.AdminBannerListQuery;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.file.service.S3FileService;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacadeAdminBannerServiceImpl implements FacadeAdminBannerService {

    private final BannerService bannerService;
    private final FileMetaDataService fileMetaDataService;
    private final S3FileService s3FileService;

    @Override
    @Transactional
    public Long create(CreateBannerCommand command) {
        Long savedBannerId = bannerService.save(command.toEntity());
        fileMetaDataService.updateStatusToCoupled(command.webImageId(), DomainType.BANNER_WEB_IMAGE, savedBannerId);
        fileMetaDataService.updateStatusToCoupled(
                command.mobileImageId(), DomainType.BANNER_MOBILE_IMAGE, savedBannerId);
        return savedBannerId;
    }

    @Override
    public List<AdminBannerListQuery> findAll() {
        List<Banner> banners = bannerService.findAll();
        return banners.stream()
                .map(banner -> {
                            List<FileMetaData> bannerImages = fileMetaDataService.getCoupledAllByEntityId(
                                    banner.getId());
                            UploadedFileUrlQuery bannerWebImageUrlQuery = null;
                            UploadedFileUrlQuery bannerMobileImageUrlQuery = null;
                            for (FileMetaData bannerImage : bannerImages) {
                                if (bannerImage.getDomainType().equals(DomainType.BANNER_WEB_IMAGE)) {
                                    bannerWebImageUrlQuery = s3FileService.getUploadedFileUrl(bannerImage.getFileKey());
                                }
                                bannerMobileImageUrlQuery = s3FileService.getUploadedFileUrl(bannerImage.getFileKey());
                            }
                            return AdminBannerListQuery.of(banner, bannerWebImageUrlQuery, bannerMobileImageUrlQuery);
                        }
                )
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long bannerId) {
        bannerService.delete(bannerId);
        fileMetaDataService.updateStatusToDelete(DomainType.BANNER_WEB_IMAGE, bannerId);
        fileMetaDataService.updateStatusToDelete(DomainType.BANNER_MOBILE_IMAGE, bannerId);
    }

}
