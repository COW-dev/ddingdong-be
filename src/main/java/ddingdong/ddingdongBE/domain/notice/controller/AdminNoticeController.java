package ddingdong.ddingdongBE.domain.notice.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.notice.api.AdminNoticeApi;
import ddingdong.ddingdongBE.domain.notice.controller.dto.request.CreateNoticeRequest;
import ddingdong.ddingdongBE.domain.notice.controller.dto.request.UpdateNoticeRequest;
import ddingdong.ddingdongBE.domain.notice.service.FacadeAdminNoticeService;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class AdminNoticeController implements AdminNoticeApi {

    private final FacadeAdminNoticeService facadeAdminNoticeService;

    @Override
    public void createNotice(@ModelAttribute CreateNoticeRequest request,
                               @AuthenticationPrincipal PrincipalDetails principalDetails,
                               @RequestPart(name = "thumbnailImages", required = false) List<MultipartFile> images,
                               @RequestPart(name = "uploadFiles", required = false) List<MultipartFile> files) {
        User adminUser = principalDetails.getUser();
        facadeAdminNoticeService.create(request.toCommand(adminUser, images, files));
    }

    @Override
    public void updateNotice(@PathVariable Long noticeId,
                             @ModelAttribute UpdateNoticeRequest request,
                             @RequestPart(name = "thumbnailImages", required = false) List<MultipartFile> images,
                             @RequestPart(name = "uploadFiles", required = false) List<MultipartFile> files
    ) {
        facadeAdminNoticeService.update(request.toCommand(noticeId, images, files));
    }

    @Override
    public void deleteNotice(@PathVariable Long noticeId) {
        facadeAdminNoticeService.delete(noticeId);
    }
}
