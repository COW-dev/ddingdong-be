package ddingdong.ddingdongBE.domain.fileinformation.service;

import ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory;
import ddingdong.ddingdongBE.domain.fileinformation.entity.FileInformation;
import ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory;
import ddingdong.ddingdongBE.domain.fileinformation.repository.FileInformationRepository;
import ddingdong.ddingdongBE.file.FileStore;
import ddingdong.ddingdongBE.file.dto.UploadFileDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FileInformationService {

    private final FileInformationRepository fileInformationRepository;
    private final FileStore fileStore;

    public void create(Long parentId, UploadFileDto uploadFileDto, FileTypeCategory fileTypeCategory,
                       FileDomainCategory fileDomainCategory) {
        FileInformation clubFileInformation = FileInformation.builder()
                .uploadName(uploadFileDto.getUploadFileName())
                .storedName(uploadFileDto.getStoredFileName())
                .fileTypeCategory(fileTypeCategory)
                .fileDomainCategory(fileDomainCategory)
                .findParam(fileTypeCategory.getFileType() + fileDomainCategory.getFileDomain() + parentId).build();

        fileInformationRepository.save(clubFileInformation);
    }

    @Transactional(readOnly = true)
    public List<String> getImageUrls(String findParam) {
        return fileInformationRepository.findByFindParam(findParam).stream()
                .map(fileInformation -> fileStore.getImageUrlPrefix() + fileInformation.getFileTypeCategory()
                        .getFileType()
                        + fileInformation.getFileDomainCategory().getFileDomain() + fileInformation.getStoredName())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FileInformation> getImageInformation(String findParam) {
        return fileInformationRepository.findByFindParam(findParam);
    }

    public void delete(FileInformation clubFileInformation) {
        fileInformationRepository.delete(clubFileInformation);
    }

}
