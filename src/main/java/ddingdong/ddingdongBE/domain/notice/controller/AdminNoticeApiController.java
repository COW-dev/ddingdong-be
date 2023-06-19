package ddingdong.ddingdongBE.domain.notice.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.notice.controller.dto.request.RegisterNoticeRequest;
import ddingdong.ddingdongBE.domain.notice.controller.dto.request.UpdateNoticeRequest;
import ddingdong.ddingdongBE.domain.notice.service.NoticeService;
import ddingdong.ddingdongBE.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/notices")
@RequiredArgsConstructor
public class AdminNoticeApiController {

    private final NoticeService noticeService;

    @PostMapping
    public void registerNotice(@ModelAttribute RegisterNoticeRequest request,
                               @AuthenticationPrincipal PrincipalDetails principalDetails) {
        User adminUser = principalDetails.getUser();

        noticeService.register(adminUser, request);
    }

    @PatchMapping("/{noticeId}")
    public void updateNotice(@PathVariable Long noticeId,
                             @ModelAttribute UpdateNoticeRequest request) {
        noticeService.update(noticeId, request);
    }

}
