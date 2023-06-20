package ddingdong.ddingdongBE.domain.notice.service;

import static ddingdong.ddingdongBE.common.exception.ErrorMessage.*;
import static ddingdong.ddingdongBE.domain.imageinformation.entity.ImageCategory.NOTICE;

import ddingdong.ddingdongBE.domain.imageinformation.service.ImageInformationService;
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
    private final ImageInformationService imageInformationService;

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
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_NOTICE.getText()));

        List<String> imageUrls = imageInformationService.getImageUrls(NOTICE.getFilePath() + noticeId);

        return DetailNoticeResponse.of(notice, imageUrls);
    }

    public void update(Long noticeId, UpdateNoticeRequest request) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_NOTICE.getText()));

        notice.update(request);
    }

    public void delete(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_NOTICE.getText()));

        noticeRepository.delete(notice);
    }

}
