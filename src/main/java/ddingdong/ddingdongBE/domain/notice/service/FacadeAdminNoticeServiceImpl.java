package ddingdong.ddingdongBE.domain.notice.service;

import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.domain.filemetadata.service.dto.FileMetaDataIdOrderDto;
import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import ddingdong.ddingdongBE.domain.notice.service.dto.command.CreateNoticeCommand;
import ddingdong.ddingdongBE.domain.notice.service.dto.command.CreateNoticeCommand.FileInfo;
import ddingdong.ddingdongBE.domain.notice.service.dto.command.CreateNoticeCommand.ImageInfo;
import ddingdong.ddingdongBE.domain.notice.service.dto.command.UpdateNoticeCommand;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FacadeAdminNoticeServiceImpl implements FacadeAdminNoticeService {

    private final NoticeService noticeService;
    private final FileMetaDataService fileMetaDataService;

    @Transactional
    public void create(CreateNoticeCommand command) {
        Notice notice = command.toEntity();
        Long createdNoticeId = noticeService.save(notice);

        List<FileMetaDataIdOrderDto> imageFileMetaDataIdOrderDtos = command.imageInfos().stream()
                .map(imageInfo -> FileMetaDataIdOrderDto.of(imageInfo.imagId(), imageInfo.order()))
                .toList();

        List<FileMetaDataIdOrderDto> fileFileMetaDataIdOrderDtos = command.fileInfos().stream()
                .map(fileInfo -> FileMetaDataIdOrderDto.of(fileInfo.fileId(), fileInfo.order()))
                .toList();

        fileMetaDataService.updateStatusToCoupledWithOrder(
                imageFileMetaDataIdOrderDtos,
                DomainType.NOTICE_IMAGE,
                createdNoticeId);
        fileMetaDataService.updateStatusToCoupledWithOrder(
                fileFileMetaDataIdOrderDtos,
                DomainType.NOTICE_FILE,
                createdNoticeId);
    }

    @Transactional
    public void update(UpdateNoticeCommand command) {
        Notice notice = noticeService.getById(command.noticeId());
        notice.update(command.toEntity());

        List<FileMetaDataIdOrderDto> imageFileMetaDataIdOrderDtos = command.imageInfos().stream()
                .map(imageInfo -> FileMetaDataIdOrderDto.of(imageInfo.imagId(), imageInfo.order()))
                .toList();

        List<FileMetaDataIdOrderDto> fileFileMetaDataIdOrderDtos = command.fileInfos().stream()
                .map(fileInfo -> FileMetaDataIdOrderDto.of(fileInfo.fileId(), fileInfo.order()))
                .toList();

        fileMetaDataService.updateWithOrder(imageFileMetaDataIdOrderDtos, DomainType.NOTICE_IMAGE,
                command.noticeId());
        fileMetaDataService.updateWithOrder(fileFileMetaDataIdOrderDtos, DomainType.NOTICE_FILE,
                command.noticeId());
    }

    @Transactional
    public void delete(Long noticeId) {
        Notice notice = noticeService.getById(noticeId);
        noticeService.delete(notice);

        fileMetaDataService.updateStatusToDelete(DomainType.NOTICE_IMAGE, noticeId);
        fileMetaDataService.updateStatusToDelete(DomainType.NOTICE_FILE, noticeId);
    }

}
