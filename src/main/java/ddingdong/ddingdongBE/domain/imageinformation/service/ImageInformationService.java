package ddingdong.ddingdongBE.domain.imageinformation.service;

import ddingdong.ddingdongBE.domain.imageinformation.entity.ImageCategory;
import ddingdong.ddingdongBE.domain.imageinformation.entity.ImageInformation;
import ddingdong.ddingdongBE.domain.imageinformation.repository.ImageInformationRepository;
import ddingdong.ddingdongBE.file.dto.UploadFileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageInformationService {

    private final ImageInformationRepository imageInformationRepository;

    public void create(Long parentId, UploadFileDto uploadFileDto, ImageCategory imageCategory) {
        ImageInformation clubImageInformation = ImageInformation.builder()
                .uploadName(uploadFileDto.getUploadFileName())
                .storedName(uploadFileDto.getStoredFileName())
                .imageCategory(imageCategory)
                .findParam(imageCategory.getFilePath() + parentId).build();

        imageInformationRepository.save(clubImageInformation);
    }

}
