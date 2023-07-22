package ddingdong.ddingdongBE.domain.banner.service;

import static ddingdong.ddingdongBE.common.exception.ErrorMessage.*;

import ddingdong.ddingdongBE.domain.banner.controller.dto.request.CreateBannerRequest;
import ddingdong.ddingdongBE.domain.banner.controller.dto.request.UpdateBannerRequest;
import ddingdong.ddingdongBE.domain.banner.controller.dto.response.BannerResponse;
import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import ddingdong.ddingdongBE.domain.banner.repository.BannerRepository;
import ddingdong.ddingdongBE.domain.imageinformation.entity.ImageCategory;
import ddingdong.ddingdongBE.domain.imageinformation.service.ImageInformationService;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;
    private final ImageInformationService imageInformationService;

    public Long createBanner(User user, CreateBannerRequest request) {
        Banner banner = request.toEntity(user);
        Banner savedBanner = bannerRepository.save(banner);
        return savedBanner.getId();
    }

    @Transactional(readOnly = true)
    public List<BannerResponse> getAllBanners() {
        List<BannerResponse> bannerResponses = new ArrayList<>();

        List<Banner> banners = bannerRepository.findAll();
        for (Banner banner : banners) {
            String bannerImageUrl = imageInformationService.getImageUrls(
                            ImageCategory.BANNER.getFilePath() + banner.getId()).get(0);
            bannerResponses.add(BannerResponse.of(banner, bannerImageUrl));
        }
        return bannerResponses;
    }

    public void updateBanner(Long bannerId, UpdateBannerRequest request) {
        Banner banner = bannerRepository.findById(bannerId)
                .orElseThrow(() -> new IllegalArgumentException(NO_SUCH_BANNER.getText()));

        banner.update(request);
    }

}
