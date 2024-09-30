package ddingdong.ddingdongBE.domain.notice.service;

import static ddingdong.ddingdongBE.common.exception.ErrorMessage.NO_SUCH_NOTICE;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.NOTICE;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.FILE;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.IMAGE;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.fileinformation.service.FileInformationService;
import ddingdong.ddingdongBE.domain.notice.controller.dto.response.NoticeListResponse;
import ddingdong.ddingdongBE.domain.notice.controller.dto.response.NoticeResponse;
import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import ddingdong.ddingdongBE.domain.notice.repository.NoticeRepository;
import ddingdong.ddingdongBE.file.dto.FileResponse;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
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

    @Transactional(readOnly = true)
    public List<Notice> getNotices() {
        return noticeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public NoticeResponse getNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_NOTICE.getText()));

        List<String> imageUrls = fileInformationService.getImageUrls(IMAGE.getFileType() + NOTICE.getFileDomain() + noticeId);
        List<FileResponse> fileUrls = fileInformationService.getFileUrls(FILE.getFileType() + NOTICE.getFileDomain() + noticeId);

        return NoticeResponse.of(notice, imageUrls, fileUrls);
    }
}
