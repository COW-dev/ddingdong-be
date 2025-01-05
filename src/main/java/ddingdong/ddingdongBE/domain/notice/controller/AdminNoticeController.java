package ddingdong.ddingdongBE.domain.notice.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.notice.api.AdminNoticeApi;
import ddingdong.ddingdongBE.domain.notice.controller.dto.request.CreateNoticeRequest;
import ddingdong.ddingdongBE.domain.notice.controller.dto.request.UpdateNoticeRequest;
import ddingdong.ddingdongBE.domain.notice.service.FacadeAdminNoticeService;
import ddingdong.ddingdongBE.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminNoticeController implements AdminNoticeApi {

    private final FacadeAdminNoticeService facadeAdminNoticeService;

    @Override
    public void createNotice(CreateNoticeRequest request, PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        facadeAdminNoticeService.create(request.toCommand(user));
    }

    @Override
    public void updateNotice(Long noticeId, UpdateNoticeRequest request) {
        facadeAdminNoticeService.update(request.toCommand(noticeId));
    }

    @Override
    public void deleteNotice(Long noticeId) {
        facadeAdminNoticeService.delete(noticeId);
    }

}
