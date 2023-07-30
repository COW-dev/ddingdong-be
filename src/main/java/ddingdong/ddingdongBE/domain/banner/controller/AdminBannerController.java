package ddingdong.ddingdongBE.domain.banner.controller;

import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.IMAGE;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.BANNER;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.banner.controller.dto.request.CreateBannerRequest;
import ddingdong.ddingdongBE.domain.banner.controller.dto.request.UpdateBannerRequest;
import ddingdong.ddingdongBE.domain.banner.service.BannerService;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.file.service.FileService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/banners")
@RequiredArgsConstructor
public class AdminBannerController {

    private final BannerService bannerService;
    private final FileService fileService;

    @PostMapping
    public void createBanner(@AuthenticationPrincipal PrincipalDetails principalDetails,
                             @ModelAttribute CreateBannerRequest request,
                             @RequestPart(name = "uploadFiles", required = false) List<MultipartFile> bannerImages) {
        User adminUser = principalDetails.getUser();
        Long createdBannerId = bannerService.createBanner(adminUser, request);

        fileService.uploadFile(createdBannerId, bannerImages, IMAGE, BANNER);
    }

    @PatchMapping("/{bannerId}")
    public void updateBanner(@PathVariable Long bannerId,
                             @ModelAttribute UpdateBannerRequest request,
                             @RequestPart(name = "uploadFiles", required = false) List<MultipartFile> bannerImages) {

        bannerService.updateBanner(bannerId, request);
        fileService.deleteFile(bannerId, IMAGE, BANNER);
        fileService.uploadFile(bannerId, bannerImages, IMAGE, BANNER);
    }

    @DeleteMapping("/{bannerId}")
    public void deleteBanner(@PathVariable Long bannerId) {
        bannerService.deleteBanner(bannerId);

        fileService.deleteFile(bannerId, IMAGE, BANNER);
    }
}
