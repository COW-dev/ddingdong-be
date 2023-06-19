package ddingdong.ddingdongBE.file.service;


import ddingdong.ddingdongBE.domain.imageinformation.entity.ImageCategory;
import ddingdong.ddingdongBE.domain.imageinformation.entity.ImageInformation;
import ddingdong.ddingdongBE.domain.imageinformation.service.ImageInformationService;
import ddingdong.ddingdongBE.file.FileStore;
import ddingdong.ddingdongBE.file.dto.UploadFileDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileService {

    private final ImageInformationService imageInformationService;
    private final FileStore fileStore;

    public void uploadImageFile(Long parentId, List<MultipartFile> images, ImageCategory imageCategory) {
        if (images != null && !images.isEmpty()) {
            List<UploadFileDto> uploadFileDtos = fileStore.storeFile(images, imageCategory.getFilePath());
            for (UploadFileDto uploadFileDto : uploadFileDtos) {
                imageInformationService.create(parentId, uploadFileDto, imageCategory);
            }
        }
    }

    public void deleteClubImage(Long parentId, ImageCategory imageCategory) {
        List<ImageInformation> imageInformationList = imageInformationService.getImageInformation(
                imageCategory.getFilePath() + parentId);

        for (ImageInformation imageInformation : imageInformationList) {
            fileStore.deleteFile(imageCategory.getFilePath(), imageInformation.getStoredName());
            imageInformationService.delete(imageInformation);
        }
    }

}
