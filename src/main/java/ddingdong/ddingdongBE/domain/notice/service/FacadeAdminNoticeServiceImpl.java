package ddingdong.ddingdongBE.domain.notice.service;

import ddingdong.ddingdongBE.common.converter.FileInfoConverter;
import ddingdong.ddingdongBE.common.vo.FileInfo;
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
    private final FileInfoConverter fileInfoConverter;

    @Transactional
    public void create(CreateNoticeCommand command) {
        String fileInfoJsonString = fileInfoConverter.convertToString(command.fileInfos());
        Notice notice = command.toEntity(fileInfoJsonString);
        noticeService.save(notice);

        createFileMetaData(command.imageKeys());

        List<String> fileKeys = command.fileInfos().stream()
            .map(FileInfo::fileKey)
            .toList();
        createFileMetaData(fileKeys);
    }

    @Transactional
    public void update(UpdateNoticeCommand command) {
        Notice notice = noticeService.getById(command.noticeId());

        String fileInfoJsonString = fileInfoConverter.convertToString(command.fileInfos());
        notice.update(command.toEntity(fileInfoJsonString));

        createFileMetaData(command.imageKeys());

        List<String> fileKeys = command.fileInfos().stream()
            .map(FileInfo::fileKey)
            .toList();
        createFileMetaData(fileKeys);
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
