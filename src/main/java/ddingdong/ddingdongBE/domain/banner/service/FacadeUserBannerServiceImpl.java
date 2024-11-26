package ddingdong.ddingdongBE.domain.banner.service;

import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import ddingdong.ddingdongBE.domain.banner.service.dto.query.AdminBannerListQuery;
import ddingdong.ddingdongBE.domain.banner.service.dto.query.UserBannerListQuery;
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
public class FacadeUserBannerServiceImpl implements FacadeUserBannerService {

    private final BannerService bannerService;
    private final FileMetaDataService fileMetaDataService;
    private final S3FileService s3FileService;

    @Override
    public List<UserBannerListQuery> findAll() {
        List<Banner> banners = bannerService.findAll();
        return banners.stream()
                .map(banner -> {
                            List<FileMetaData> bannerImages = fileMetaDataService
                                    .getCoupledAllByEntityId(banner.getId());
                            UploadedFileUrlQuery bannerWebImageUrlQuery = null;
                            UploadedFileUrlQuery bannerMobileImageUrlQuery = null;
                            for (FileMetaData bannerImage : bannerImages) {
                                if (bannerImage.getDomainType().equals(DomainType.BANNER_WEB_IMAGE)) {
                                    bannerWebImageUrlQuery = s3FileService.getUploadedFileUrl(bannerImage.getFileKey());
                                }
                                bannerMobileImageUrlQuery = s3FileService.getUploadedFileUrl(bannerImage.getFileKey());
                            }
                            return UserBannerListQuery.of(banner, bannerWebImageUrlQuery, bannerMobileImageUrlQuery);
                        }
                )
                .toList();
    }
}
