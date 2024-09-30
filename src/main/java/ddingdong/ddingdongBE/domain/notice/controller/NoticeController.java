package ddingdong.ddingdongBE.domain.notice.controller;

import ddingdong.ddingdongBE.domain.notice.api.NoticeApi;
import ddingdong.ddingdongBE.domain.notice.controller.dto.response.NoticeListResponse;
import ddingdong.ddingdongBE.domain.notice.controller.dto.response.NoticeResponse;
import ddingdong.ddingdongBE.domain.notice.service.FacadeNoticeService;
import ddingdong.ddingdongBE.domain.notice.service.dto.query.NoticeListQuery;
import ddingdong.ddingdongBE.domain.notice.service.dto.query.NoticeQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NoticeController implements NoticeApi {

    private final FacadeNoticeService facadeNoticeService;

    @Override
    public List<NoticeListResponse> getNotices() {
        List<NoticeListQuery> queries = facadeNoticeService.getNotices();
        return queries.stream()
            .map(NoticeListResponse::from)
            .toList();
    }

    @Override
    public NoticeResponse getNotice(@PathVariable Long noticeId) {
        NoticeQuery query = facadeNoticeService.getNotice(noticeId);
        return NoticeResponse.from(query);
    }
}
