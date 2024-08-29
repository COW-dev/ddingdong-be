package ddingdong.ddingdongBE.domain.notice.controller;

import ddingdong.ddingdongBE.domain.notice.api.NoticeApi;
import ddingdong.ddingdongBE.domain.notice.controller.dto.request.GetAllNoticeByPageRequest;
import ddingdong.ddingdongBE.domain.notice.controller.dto.response.DetailNoticeResponse;
import ddingdong.ddingdongBE.domain.notice.controller.dto.response.GetAllNoticeByPageResponse;
import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import ddingdong.ddingdongBE.domain.notice.service.NoticeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NoticeApiController implements NoticeApi {

    private final NoticeService noticeService;

    @GetMapping
    public GetAllNoticeByPageResponse getAllNotices(GetAllNoticeByPageRequest request) {
        List<Notice> notices = noticeService.getAllNotices(
            request.getPage(),
            request.getLimit()
        );

        return GetAllNoticeByPageResponse.from(notices);
    }

    @GetMapping("/{noticeId}")
    public DetailNoticeResponse getNoticeDetail(@PathVariable Long noticeId) {
        return noticeService.getNotice(noticeId);
    }

}
