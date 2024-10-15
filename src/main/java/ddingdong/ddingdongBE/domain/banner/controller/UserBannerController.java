package ddingdong.ddingdongBE.domain.banner.controller;

import ddingdong.ddingdongBE.domain.banner.service.GeneralBannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/server/banners")
@RequiredArgsConstructor
public class UserBannerController {

    private final GeneralBannerService generalBannerService;

//    @GetMapping
//    public List<BannerResponse> getBanners() {
//        return generalBannerService.getAllBanners();
//    }

}
