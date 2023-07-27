package ddingdong.ddingdongBE.domain.notice.controller;

import static ddingdong.ddingdongBE.domain.imageinformation.entity.ImageCategory.*;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.notice.controller.dto.request.RegisterNoticeRequest;
import ddingdong.ddingdongBE.domain.notice.controller.dto.request.UpdateNoticeRequest;
import ddingdong.ddingdongBE.domain.notice.service.NoticeService;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.file.service.FileService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
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
@RequestMapping("/api/admin/notices")
@RequiredArgsConstructor
public class AdminNoticeApiController {

    private final NoticeService noticeService;
    private final FileService fileService;

    @PostMapping
    public void registerNotice(@ModelAttribute RegisterNoticeRequest request,
                               @AuthenticationPrincipal PrincipalDetails principalDetails,
                               @RequestPart(name = "uploadFiles", required = false) List<MultipartFile> images) {
        User adminUser = principalDetails.getUser();
        Long registeredNoticeId = noticeService.register(adminUser, request);
        fileService.uploadImageFile(registeredNoticeId, images, NOTICE);
    }

    @PatchMapping("/{noticeId}")
    public void updateNotice(@PathVariable Long noticeId,
                             @ModelAttribute UpdateNoticeRequest request,
                             @RequestPart(name = "uploadFiles", required = false) List<MultipartFile> images) {
        noticeService.update(noticeId, request);
        fileService.deleteImageFile(noticeId, NOTICE);
        fileService.uploadImageFile(noticeId, images, NOTICE);
    }

    @DeleteMapping("/{noticeId}")
    public void deleteNotice(@PathVariable Long noticeId) {
        noticeService.delete(noticeId);
        fileService.deleteImageFile(noticeId, NOTICE);
    }

}
