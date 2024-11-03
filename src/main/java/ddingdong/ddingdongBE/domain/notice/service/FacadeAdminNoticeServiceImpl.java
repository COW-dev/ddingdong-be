package ddingdong.ddingdongBE.domain.notice.service;

import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import ddingdong.ddingdongBE.domain.notice.service.dto.command.CreateNoticeCommand;
import ddingdong.ddingdongBE.domain.notice.service.dto.command.UpdateNoticeCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FacadeAdminNoticeServiceImpl implements FacadeAdminNoticeService {

    private final NoticeServiceImpl noticeService;
    private final FileMetaDataService fileMetaDataService;

    @Transactional
    public void create(CreateNoticeCommand command) {
        Notice notice = command.toEntity();
        Long createdNoticeId = noticeService.save(notice);

        fileMetaDataService.updateToCoupled(command.images(), DomainType.NOTICE_IMAGE,
            createdNoticeId);
        fileMetaDataService.updateToCoupled(command.files(), DomainType.NOTICE_FILE,
            createdNoticeId);
    }

    @Transactional
    public void update(UpdateNoticeCommand command) {
        Notice notice = noticeService.getById(command.noticeId());
        notice.update(command.toEntity());

        fileMetaDataService.update(command.images(), DomainType.NOTICE_IMAGE,
            command.noticeId());
        fileMetaDataService.update(command.images(), DomainType.NOTICE_FILE,
            command.noticeId());
    }

    @Transactional
    public void delete(Long noticeId) {
        Notice notice = noticeService.getById(noticeId);
        noticeService.delete(notice);

        fileMetaDataService.updateToDelete(DomainType.NOTICE_IMAGE, noticeId);
        fileMetaDataService.updateToDelete(DomainType.NOTICE_FILE, noticeId);
    }

}
