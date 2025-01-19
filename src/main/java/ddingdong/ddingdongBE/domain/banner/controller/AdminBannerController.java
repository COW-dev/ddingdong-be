package ddingdong.ddingdongBE.domain.banner.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.banner.api.AdminBannerApi;
import ddingdong.ddingdongBE.domain.banner.controller.dto.request.CreateBannerRequest;
import ddingdong.ddingdongBE.domain.banner.controller.dto.response.AdminBannerListResponse;
import ddingdong.ddingdongBE.domain.banner.service.FacadeAdminBannerService;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminBannerController implements AdminBannerApi {

    private final FacadeAdminBannerService facadeAdminBannerService;

    @Override
    public void createBanner(PrincipalDetails principalDetails, CreateBannerRequest request) {
        User user = principalDetails.getUser();
        facadeAdminBannerService.create(request.toCommand(user));
    }

    @Override
    public List<AdminBannerListResponse> findAllBanners() {
        return facadeAdminBannerService.findAll().stream()
                .map(AdminBannerListResponse::from)
                .toList();
    }

    @Override
    public void deleteBanner(Long bannerId) {
        facadeAdminBannerService.delete(bannerId);
    }

}
