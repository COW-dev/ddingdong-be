package ddingdong.ddingdongBE.domain.banner.controller;

import ddingdong.ddingdongBE.domain.banner.controller.dto.response.BannerResponse;
import ddingdong.ddingdongBE.domain.banner.service.BannerService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/server/banners")
@RequiredArgsConstructor
public class UserBannerController {

    private final BannerService bannerService;

    @GetMapping
    public List<BannerResponse> getBanners() {
        return bannerService.getAllBanners();
    }

}
