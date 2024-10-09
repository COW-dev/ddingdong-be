package ddingdong.ddingdongBE.domain.notice.service;

import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.NOTICE;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.FILE;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.IMAGE;

import ddingdong.ddingdongBE.domain.fileinformation.service.FileInformationService;
import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import ddingdong.ddingdongBE.domain.notice.service.dto.query.NoticeListQuery;
import ddingdong.ddingdongBE.domain.notice.service.dto.query.NoticeQuery;
import ddingdong.ddingdongBE.file.dto.FileResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FacadeNoticeService {

    private final NoticeService noticeService;
    private final FileInformationService fileInformationService;

    public List<NoticeListQuery> getNotices() {
        List<Notice> notices = noticeService.getNotices();
        return notices.stream()
            .map(NoticeListQuery::from)
            .toList();
    }

    public NoticeQuery getNotice(Long noticeId) {
        Notice notice = noticeService.getById(noticeId);
        List<String> imageUrls = fileInformationService.getImageUrls(IMAGE.getFileType() + NOTICE.getFileDomain() + noticeId);
        List<FileResponse> fileUrls = fileInformationService.getFileUrls(FILE.getFileType() + NOTICE.getFileDomain() + noticeId);
        return NoticeQuery.of(notice, imageUrls, fileUrls);
    }
}
