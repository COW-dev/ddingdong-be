package ddingdong.ddingdongBE.domain.notice.service;

import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import java.util.List;

public interface NoticeService {

    Long save(Notice notice);

    void delete(Notice notice);

    Notice getById(Long noticeId);

    List<Notice> getNoticeListByPage(int page, int limit);

    int getNoticePageCount();

}
