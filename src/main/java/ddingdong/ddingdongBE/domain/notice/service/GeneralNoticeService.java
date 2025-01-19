package ddingdong.ddingdongBE.domain.notice.service;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import ddingdong.ddingdongBE.domain.notice.repository.NoticeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeneralNoticeService implements NoticeService {

    public static final int NOTICE_COUNT_FOR_PAGE = 10;

    private final NoticeRepository noticeRepository;

    @Override
    @Transactional
    public Long save(Notice notice) {
        Notice savedNotice = noticeRepository.save(notice);
        return savedNotice.getId();
    }

    @Override
    @Transactional
    public void delete(Notice notice) {
        noticeRepository.delete(notice);
    }

    @Override
    public Notice getById(Long noticeId) {
        return noticeRepository.findById(noticeId)
            .orElseThrow(() ->
                new ResourceNotFound("해당 Document(ID: " + noticeId + ")" + "를 찾을 수 없습니다.")
            );
    }

    @Override
    public List<Notice> getNoticeListByPage(int page, int limit) {
        int offset = (page - 1) * limit;
        return noticeRepository.findAllByPage(limit, offset);
    }

    @Override
    public int getNoticePageCount() {
        int totalCount = noticeRepository.countAll();
        return (totalCount + NOTICE_COUNT_FOR_PAGE - 1) / NOTICE_COUNT_FOR_PAGE;
    }

}
