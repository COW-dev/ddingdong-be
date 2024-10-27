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
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    @Override
    @Transactional
    public Notice save(Notice notice) {
        return noticeRepository.save(notice);
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
        return (int) noticeRepository.countAll();
    }

}
