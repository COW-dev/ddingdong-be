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
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional
    public Notice save(Notice notice) {
        return noticeRepository.save(notice);
    }

    @Transactional
    public void update(Long noticeId, Notice updateNotice) {
        Notice notice = getById(noticeId);
        notice.update(updateNotice);
    }

    @Transactional
    public void delete(Notice notice) {
        noticeRepository.delete(notice);
    }

    public Notice getById(Long noticeId) {
        return noticeRepository.findById(noticeId)
            .orElseThrow(() -> new ResourceNotFound("해당 Document(ID: " + noticeId + ")" + "를 찾을 수 없습니다."));
    }

    public List<Notice> getNotices() {
        return noticeRepository.findAll();
    }
}
