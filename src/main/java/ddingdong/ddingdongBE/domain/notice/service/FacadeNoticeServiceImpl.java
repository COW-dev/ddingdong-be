package ddingdong.ddingdongBE.domain.notice.service;

import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import ddingdong.ddingdongBE.domain.notice.service.dto.command.GetNoticeListCommand;
import ddingdong.ddingdongBE.domain.notice.service.dto.query.NoticeListPagingQuery;
import ddingdong.ddingdongBE.domain.notice.service.dto.query.NoticeListPagingQuery.NoticeInfo;
import ddingdong.ddingdongBE.domain.notice.service.dto.query.NoticeQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FacadeNoticeServiceImpl implements FacadeNoticeService {

    private final NoticeServiceImpl noticeService;

    @Override
    public NoticeListPagingQuery getNoticeList(GetNoticeListCommand command) {
        List<Notice> notices = noticeService.getNoticeListByPage(command.page(), command.limit());

        List<NoticeInfo> noticeInfos = notices.stream()
            .map(NoticeInfo::from)
            .toList();

        int totalPage = noticeService.getNoticePageCount();

        return NoticeListPagingQuery.of(noticeInfos, totalPage);
    }

    public NoticeQuery getNotice(Long noticeId) {
        Notice notice = noticeService.getById(noticeId);

        return null;
    }

}
