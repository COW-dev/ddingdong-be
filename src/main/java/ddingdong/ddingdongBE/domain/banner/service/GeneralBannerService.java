package ddingdong.ddingdongBE.domain.banner.service;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import ddingdong.ddingdongBE.domain.banner.repository.BannerRepository;
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
        return savedBanner.getId();
    }

    @Override
    public List<Banner> findAll() {
        return bannerRepository.findAllByOrderByIdDesc();
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
