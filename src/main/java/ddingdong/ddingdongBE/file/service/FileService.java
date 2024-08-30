package ddingdong.ddingdongBE.file.service;

import ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory;
import ddingdong.ddingdongBE.domain.fileinformation.entity.FileInformation;
import ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory;
import ddingdong.ddingdongBE.domain.fileinformation.service.FileInformationService;
import ddingdong.ddingdongBE.file.FileStore;
import ddingdong.ddingdongBE.file.service.dto.UploadFileDto;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileInformationService fileInformationService;
    private final FileStore fileStore;

    public void uploadFile(Long parentId, List<MultipartFile> files, FileTypeCategory fileTypeCategory, FileDomainCategory fileDomainCategory) {
        if (files != null && !files.isEmpty()) {
            List<UploadFileDto> uploadFileDtos = fileStore.storeFile(files, fileTypeCategory.getFileType(), fileDomainCategory.getFileDomain());
            for (UploadFileDto uploadFileDto : uploadFileDtos) {
                fileInformationService.create(parentId, uploadFileDto, fileTypeCategory, fileDomainCategory);
            }
        }
    }

    public void uploadDownloadableFile(Long parentId, List<MultipartFile> files, FileTypeCategory fileTypeCategory, FileDomainCategory fileDomainCategory) {
        if (files != null && !files.isEmpty()) {
            List<UploadFileDto> uploadFileDtos = fileStore.storeDownloadableFile(files, fileTypeCategory.getFileType(), fileDomainCategory.getFileDomain());
            for (UploadFileDto uploadFileDto : uploadFileDtos) {
                fileInformationService.create(parentId, uploadFileDto, fileTypeCategory, fileDomainCategory);
            }
        }
    }

    public void deleteFile(Long parentId, FileTypeCategory fileTypeCategory, FileDomainCategory fileDomainCategory) {
        List<FileInformation> fileInformations = fileInformationService.getFileInformation(
                fileTypeCategory.getFileType() + fileDomainCategory.getFileDomain() + parentId);

        for (FileInformation fileInformation : fileInformations) {
            fileStore.deleteFile(fileTypeCategory.getFileType(), fileDomainCategory.getFileDomain(), fileInformation.getStoredName());
            fileInformationService.delete(fileInformation);
        }
    }
}
