package ddingdong.ddingdongBE.domain.banner.controller;

import static ddingdong.ddingdongBE.domain.imageinformation.entity.ImageCategory.*;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.banner.controller.dto.request.CreateBannerRequest;
import ddingdong.ddingdongBE.domain.banner.service.BannerService;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.file.service.FileService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
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

        fileService.uploadImageFile(createdBannerId, bannerImages, BANNER);
    }

}
