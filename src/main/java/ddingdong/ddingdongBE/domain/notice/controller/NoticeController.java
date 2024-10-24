package ddingdong.ddingdongBE.domain.notice.controller;

import ddingdong.ddingdongBE.domain.notice.api.NoticeApi;
import ddingdong.ddingdongBE.domain.notice.controller.dto.request.GetNoticePagingRequest;
import ddingdong.ddingdongBE.domain.notice.controller.dto.response.NoticeListResponse;
import ddingdong.ddingdongBE.domain.notice.controller.dto.response.NoticeResponse;
import ddingdong.ddingdongBE.domain.notice.service.FacadeNoticeServiceImpl;
import ddingdong.ddingdongBE.domain.notice.service.dto.query.NoticeListPagingQuery;
import ddingdong.ddingdongBE.domain.notice.service.dto.query.NoticeQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NoticeController implements NoticeApi {

    private final FacadeNoticeServiceImpl facadeNoticeServiceImpl;

    @Override
    public NoticeListResponse getNoticeList(GetNoticePagingRequest request) {
        NoticeListPagingQuery noticeListPagingQuery = facadeNoticeServiceImpl.getNoticeList(
            request.toCommand());

        return NoticeListResponse.from(noticeListPagingQuery);
    }

    @Override
    public NoticeResponse getNotice(@PathVariable Long noticeId) {
        NoticeQuery query = facadeNoticeServiceImpl.getNotice(noticeId);
        return NoticeResponse.from(query);
    }

}
