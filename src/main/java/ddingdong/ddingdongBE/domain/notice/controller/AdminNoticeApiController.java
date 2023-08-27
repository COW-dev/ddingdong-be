package ddingdong.ddingdongBE.domain.notice.controller;

import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.*;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.NOTICE;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.notice.controller.dto.request.RegisterNoticeRequest;
import ddingdong.ddingdongBE.domain.notice.controller.dto.request.UpdateNoticeRequest;
import ddingdong.ddingdongBE.domain.notice.service.NoticeService;
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
@RequestMapping("/server/admin/notices")
@RequiredArgsConstructor
public class AdminNoticeApiController {

    private final NoticeService noticeService;
    private final FileService fileService;

    @PostMapping
    public void registerNotice(@ModelAttribute RegisterNoticeRequest request,
                               @AuthenticationPrincipal PrincipalDetails principalDetails,
                               @RequestPart(name = "thumbnailImages", required = false) List<MultipartFile> images,
                               @RequestPart(name = "uploadFiles", required = false) List<MultipartFile> files) {
        User adminUser = principalDetails.getUser();
        Long registeredNoticeId = noticeService.register(adminUser, request);

        fileService.uploadFile(registeredNoticeId, images, IMAGE, NOTICE);
        fileService.uploadDownloadableFile(registeredNoticeId, files, FILE, NOTICE);
    }

    @PatchMapping("/{noticeId}")
    public void updateNotice(@PathVariable Long noticeId,
                             @ModelAttribute UpdateNoticeRequest request,
                             @RequestPart(name = "thumbnailImages", required = false) List<MultipartFile> images,
                             @RequestPart(name = "uploadFiles", required = false) List<MultipartFile> files
    ) {
        noticeService.update(noticeId, request);

        if (images != null) {
            fileService.deleteFile(noticeId, IMAGE, NOTICE);
            fileService.uploadFile(noticeId, images, IMAGE, NOTICE);
        }

        if (files != null) {
            fileService.uploadDownloadableFile(noticeId, files, FILE, NOTICE);
        }
    }

    @DeleteMapping("/{noticeId}")
    public void deleteNotice(@PathVariable Long noticeId) {
        noticeService.delete(noticeId);

        fileService.deleteFile(noticeId, IMAGE, NOTICE);
        fileService.deleteFile(noticeId, FILE, NOTICE);
    }

}
