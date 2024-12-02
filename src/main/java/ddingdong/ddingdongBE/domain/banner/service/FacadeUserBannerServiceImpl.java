package ddingdong.ddingdongBE.domain.banner.service;

import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import ddingdong.ddingdongBE.domain.banner.service.dto.query.UserBannerListQuery;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.file.service.S3FileService;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        if (banners.isEmpty()) {
            return Collections.emptyList();
        }
        List<FileMetaData> bannerImages = fileMetaDataService.getCoupledAllByEntityIds(
                banners.stream().map(Banner::getId).toList());
        return banners.stream()
                .map(banner -> createBannerListQuery(banner, bannerImages))
                .toList();
    }

    private UserBannerListQuery createBannerListQuery(Banner banner, List<FileMetaData> bannerImages) {
        if(bannerImages.isEmpty()) {
            return UserBannerListQuery.of(banner, null, null);
        }
        Map<DomainType, FileMetaData> fileMetaDataMap = bannerImages.stream()
                .filter(fileMetaData -> fileMetaData.getEntityId().equals(banner.getId()))
                .collect(Collectors.toMap(
                        FileMetaData::getDomainType,
                        fileMetaData -> fileMetaData,
                        (existing, replacement) -> existing
                ));

        UploadedFileUrlQuery webImageUrlQuery = s3FileService.getUploadedFileUrl(
                fileMetaDataMap.get(DomainType.BANNER_WEB_IMAGE).getFileKey()
        );
        UploadedFileUrlQuery mobileImageUrlQuery = s3FileService.getUploadedFileUrl(
                fileMetaDataMap.get(DomainType.BANNER_MOBILE_IMAGE).getFileKey()
        );
        return UserBannerListQuery.of(banner, webImageUrlQuery, mobileImageUrlQuery);
    }
}
