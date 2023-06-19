package ddingdong.ddingdongBE.domain.notice.service;

import ddingdong.ddingdongBE.domain.notice.controller.dto.request.RegisterNoticeRequest;
import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import ddingdong.ddingdongBE.domain.notice.repository.NoticeRepository;
import ddingdong.ddingdongBE.domain.user.entity.User;
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

}
