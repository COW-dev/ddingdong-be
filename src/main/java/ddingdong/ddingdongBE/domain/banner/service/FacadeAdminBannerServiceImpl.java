package ddingdong.ddingdongBE.domain.banner.service;

import static ddingdong.ddingdongBE.domain.filemetadata.entity.FileCategory.BANNER_MOBILE_IMAGE;
import static ddingdong.ddingdongBE.domain.filemetadata.entity.FileCategory.BANNER_WEB_IMAGE;

import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import ddingdong.ddingdongBE.domain.banner.service.dto.command.CreateBannerCommand;
import ddingdong.ddingdongBE.domain.banner.service.dto.query.AdminBannerListQuery;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.file.service.S3FileService;
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
        fileMetaDataService.create(List.of(
                        FileMetaData.of(command.webImageKey(), BANNER_WEB_IMAGE),
                        FileMetaData.of(command.mobileImageKey(), BANNER_MOBILE_IMAGE)
                )
        );
        return bannerService.save(command.toEntity());
    }

    @Override
    public List<AdminBannerListQuery> findAll() {
        List<Banner> banners = bannerService.findAll();
        return banners.stream()
                .map(banner -> AdminBannerListQuery.of(
                                banner,
                                s3FileService.getUploadedFileUrl(banner.getWebImageKey()),
                                s3FileService.getUploadedFileUrl(banner.getMobileImageKey())
                        )
                )
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long bannerId) {
        bannerService.delete(bannerId);
    }

}
