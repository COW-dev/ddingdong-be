package ddingdong.ddingdongBE.domain.notice.service;

import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.NOTICE;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.FILE;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.IMAGE;

import ddingdong.ddingdongBE.domain.fileinformation.entity.FileInformation;
import ddingdong.ddingdongBE.domain.fileinformation.service.FileInformationService;
import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import ddingdong.ddingdongBE.domain.notice.service.dto.command.CreateNoticeCommand;
import ddingdong.ddingdongBE.domain.notice.service.dto.command.UpdateNoticeCommand;
import ddingdong.ddingdongBE.file.FileStore;
import ddingdong.ddingdongBE.file.service.FileService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FacadeAdminNoticeService {

    private final NoticeService noticeService;
    private final FileService fileService;
    private final FileInformationService fileInformationService;
    private final FileStore fileStore;

    @Transactional
    public void create(CreateNoticeCommand command) {
        Notice notice = command.toEntity();
        Notice savedNotice = noticeService.save(notice);
        Long savedNoticeId = savedNotice.getId();
        fileService.uploadFile(savedNoticeId, command.images(), IMAGE, NOTICE);
        fileService.uploadDownloadableFile(savedNoticeId, command.files(), FILE, NOTICE);
    }

    @Transactional
    public void update(UpdateNoticeCommand command) {
        deleteImageInformation(command.noticeId(), command.imgUrls());
        deleteFileInformation(command.noticeId(), command.fileUrls());
        updateImages(command.noticeId(), command.images());
        updateFiles(command.noticeId(), command.files());
        noticeService.update(command.noticeId(), command.toEntity());
    }

    @Transactional
    public void delete(Long noticeId) {
        Notice notice = noticeService.getById(noticeId);
        noticeService.delete(notice);
        fileService.deleteFile(noticeId, IMAGE, NOTICE);
        fileService.deleteFile(noticeId, FILE, NOTICE);
    }

    private void deleteImageInformation(Long noticeId, List<String> imgUrls) {
        List<FileInformation> imageInformation = fileInformationService.getFileInformation(
            IMAGE.getFileType() + NOTICE.getFileDomain() + noticeId);
        deleteInformation(imageInformation, imgUrls);
    }

    private void deleteFileInformation(Long noticeId, List<String> fileUrls) {
        List<FileInformation> fileInformation = fileInformationService.getFileInformation(
            FILE.getFileType() + NOTICE.getFileDomain() + noticeId);
        deleteInformation(fileInformation, fileUrls);
    }

    private void updateImages(Long noticeId, List<MultipartFile> images) {
        if (images != null) {
            fileService.deleteFile(noticeId, IMAGE, NOTICE);
            fileService.uploadFile(noticeId, images, IMAGE, NOTICE);
        }
    }

    private void updateFiles(Long noticeId, List<MultipartFile> files) {
        if (files != null) {
            fileService.uploadDownloadableFile(noticeId, files, FILE, NOTICE);
        }
    }

    private void deleteInformation(List<FileInformation> imageInformation, List<String> imgUrls) {
        if (imgUrls.isEmpty()) {
            fileInformationService.deleteAll(imageInformation);
            return;
        }

        List<FileInformation> deleteInformation = imageInformation.stream()
            .filter(information -> !imgUrls
                .contains(fileStore.getImageUrlPrefix() + information.getFileTypeCategory()
                    .getFileType() + information.getFileDomainCategory().getFileDomain() + information.getStoredName()))
            .toList();
        fileInformationService.deleteAll(deleteInformation);
    }
}
