package ddingdong.ddingdongBE.domain.notice.service;

import ddingdong.ddingdongBE.domain.notice.service.dto.command.GetNoticeListCommand;
import ddingdong.ddingdongBE.domain.notice.service.dto.query.NoticeListPagingQuery;
import ddingdong.ddingdongBE.domain.notice.service.dto.query.NoticeQuery;

public interface FacadeNoticeService {

    NoticeListPagingQuery getNoticeList(GetNoticeListCommand command);

    NoticeQuery getNotice(Long noticeId);

}
