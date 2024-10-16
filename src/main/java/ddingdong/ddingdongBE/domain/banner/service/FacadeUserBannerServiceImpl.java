package ddingdong.ddingdongBE.domain.banner.service;

import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import ddingdong.ddingdongBE.domain.banner.service.dto.query.UserBannerListQuery;
import ddingdong.ddingdongBE.file.service.S3FileService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacadeUserBannerServiceImpl implements FacadeUserBannerService {

    private final BannerService bannerService;
    private final S3FileService s3FileService;

    @Override
    public List<UserBannerListQuery> findAll() {
        List<Banner> banners = bannerService.findAll();
        return banners.stream()
                .map(banner -> UserBannerListQuery.of(
                                banner,
                                s3FileService.getUploadedFileUrl(banner.getWebImageKey()),
                                s3FileService.getUploadedFileUrl(banner.getMobileImageKey())
                        )
                )
                .toList();
    }
}
