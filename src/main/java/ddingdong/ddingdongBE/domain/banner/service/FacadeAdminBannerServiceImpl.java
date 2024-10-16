package ddingdong.ddingdongBE.domain.banner.service;

import static ddingdong.ddingdongBE.domain.filemetadata.entity.FileCategory.BANNER_WEB_IMAGE;

import ddingdong.ddingdongBE.domain.banner.service.dto.command.CreateBannerCommand;
import ddingdong.ddingdongBE.domain.filemetadata.service.FacadeFileMetaDataService;
import ddingdong.ddingdongBE.domain.filemetadata.service.dto.CreateFileMetaDataCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacadeAdminBannerServiceImpl implements FacadeAdminBannerService {

    private final BannerService bannerService;
    private final FacadeFileMetaDataService facadeFileMetaDataService;


    @Override
    @Transactional
    public Long create(CreateBannerCommand command) {
        CreateFileMetaDataCommand createBannerWebImageFileMetaDataCommand =
                new CreateFileMetaDataCommand(command.webImageKey(), BANNER_WEB_IMAGE);
        CreateFileMetaDataCommand createBannerMobileImageFileMetaDataCommand =
                new CreateFileMetaDataCommand(command.mobileImageKey(), BANNER_WEB_IMAGE);
        facadeFileMetaDataService.create(createBannerWebImageFileMetaDataCommand,
                createBannerMobileImageFileMetaDataCommand);

        return bannerService.save(command.toEntity());
    }

    @Override
    @Transactional
    public void delete(Long bannerId) {
        bannerService.delete(bannerId);
    }
}
