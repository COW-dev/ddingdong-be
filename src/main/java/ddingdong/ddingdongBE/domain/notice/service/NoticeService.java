package ddingdong.ddingdongBE.domain.notice.service;

import ddingdong.ddingdongBE.domain.notice.controller.dto.request.RegisterNoticeRequest;
import ddingdong.ddingdongBE.domain.notice.controller.dto.request.UpdateNoticeRequest;
import ddingdong.ddingdongBE.domain.notice.controller.dto.response.DetailNoticeResponse;
import ddingdong.ddingdongBE.domain.notice.controller.dto.response.NoticeResponse;
import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import ddingdong.ddingdongBE.domain.notice.repository.NoticeRepository;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public Long register(User user, RegisterNoticeRequest request) {
        Notice notice = request.toEntity(user);
        Notice savedNotice = noticeRepository.save(notice);

        return savedNotice.getId();
    }

    @Transactional(readOnly = true)
    public List<NoticeResponse> getAllNotices() {
        return noticeRepository.findAll().stream()
                .map(NoticeResponse::from)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public DetailNoticeResponse getNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoSuchElementException("해당 공지사항이 존재하지 않습니다."));

        return DetailNoticeResponse.from(notice);
    }

    public void update(Long noticeId, UpdateNoticeRequest request) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoSuchElementException("해당 공지사항이 존재하지 않습니다."));

        notice.update(request);
    }

    public void delete(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoSuchElementException("해당 공지사항이 존재하지 않습니다."));

        noticeRepository.delete(notice);
    }

}
