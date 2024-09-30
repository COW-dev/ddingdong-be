package ddingdong.ddingdongBE.domain.notice.service;

import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import ddingdong.ddingdongBE.domain.notice.service.dto.query.NoticeListQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FacadeNoticeService {

    private final NoticeService noticeService;

    public List<NoticeListQuery> getNotices() {
        List<Notice> notices = noticeService.getNotices();
        return notices.stream()
            .map(NoticeListQuery::from)
            .toList();
    }
}
