package ddingdong.ddingdongBE.domain.notice.service;

import ddingdong.ddingdongBE.domain.filemetadata.entity.FileCategory;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import ddingdong.ddingdongBE.domain.notice.service.dto.command.CreateNoticeCommand;
import ddingdong.ddingdongBE.domain.notice.service.dto.command.UpdateNoticeCommand;
import java.util.List;
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
        noticeService.save(notice);
        createFileMetaData(command.imageKeys());
        createFileMetaData(command.fileKeys());
    }

    @Transactional
    public void update(UpdateNoticeCommand command, Long noticeId) {
        Notice notice = noticeService.getById(noticeId);
        notice.update(command.toEntity());
        createFileMetaData(command.imageKeys());
        createFileMetaData(command.fileKeys());
    }

    @Transactional
    public void delete(Long noticeId) {
        Notice notice = noticeService.getById(noticeId);
        noticeService.delete(notice);
    }

    private void createFileMetaData(List<String> fileKeys) {
        if (!fileKeys.isEmpty()) {
            fileMetaDataService.create(
                fileKeys.stream()
                    .map(fileKey -> FileMetaData.of(fileKey, FileCategory.NOTICE_FILE))
                    .toList()
            );
        }
    }

}
