package ddingdong.ddingdongBE.domain.banner.service;

import static ddingdong.ddingdongBE.common.exception.ErrorMessage.*;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.banner.controller.dto.request.CreateBannerRequest;
import ddingdong.ddingdongBE.domain.banner.controller.dto.request.UpdateBannerRequest;
import ddingdong.ddingdongBE.domain.banner.controller.dto.response.BannerResponse;
import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import ddingdong.ddingdongBE.domain.banner.repository.BannerRepository;
import ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory;
import ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory;
import ddingdong.ddingdongBE.domain.fileinformation.service.FileInformationService;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GeneralBannerService implements BannerService {

    private final BannerRepository bannerRepository;

    @Override
    @Transactional
    public Long save(Banner banner) {
        Banner savedBanner = bannerRepository.save(banner);
        return banner.getId();
    }

    @Override
    public List<Banner> findAll() {
        return bannerRepository.findAll();
    }

    @Override
    @Transactional
    public void update(Long bannerId, Banner updatedBanner) {
        Banner banner = getBanner(bannerId);
        banner.update(updatedBanner);
    }

    @Override
    @Transactional
    public void delete(Long bannerId) {
        Banner banner = getBanner(bannerId);
        bannerRepository.delete(banner);
    }

    private Banner getBanner(Long bannerId) {
        return bannerRepository.findById(bannerId)
                .orElseThrow(() -> new ResourceNotFound("Banner(bannerId=" + bannerId + ")를 찾을 수 없습니다."));
    }
}
