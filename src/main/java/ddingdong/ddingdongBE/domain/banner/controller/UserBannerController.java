package ddingdong.ddingdongBE.domain.banner.controller;

import ddingdong.ddingdongBE.domain.banner.api.UserBannerApi;
import ddingdong.ddingdongBE.domain.banner.controller.dto.response.UserBannerListResponse;
import ddingdong.ddingdongBE.domain.banner.service.FacadeUserBannerService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/server/banners")
@RequiredArgsConstructor
public class UserBannerController implements UserBannerApi {

    private final FacadeUserBannerService facadeUserBannerService;

    @Override
    public List<UserBannerListResponse> findAllBanners() {
        return facadeUserBannerService.findAll().stream()
                .map(UserBannerListResponse::from)
                .toList();
    }

}
