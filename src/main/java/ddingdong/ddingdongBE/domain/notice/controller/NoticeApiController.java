package ddingdong.ddingdongBE.domain.notice.controller;

import ddingdong.ddingdongBE.domain.notice.controller.dto.response.DetailNoticeResponse;
import ddingdong.ddingdongBE.domain.notice.controller.dto.response.NoticeResponse;
import ddingdong.ddingdongBE.domain.notice.service.NoticeService;

import java.util.List;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/server/notices")
@RequiredArgsConstructor
public class NoticeApiController {

    private final NoticeService noticeService;

    @GetMapping
    public List<NoticeResponse> getAllNotices() {
        return noticeService.getAllNotices();
    }

    @GetMapping("/{noticeId}")
    public DetailNoticeResponse getNotice(@PathVariable Long noticeId) {
        return noticeService.getNotice(noticeId);
    }

}
