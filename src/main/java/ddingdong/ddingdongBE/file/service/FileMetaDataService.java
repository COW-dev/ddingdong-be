package ddingdong.ddingdongBE.file.service;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.file.entity.FileCategory;
import ddingdong.ddingdongBE.file.entity.FileMetaData;
import ddingdong.ddingdongBE.file.repository.FileMetaDataRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileMetaDataService {

    private final FileMetaDataRepository fileMetaDataRepository;

    @Transactional
    public void save(FileMetaData fileMetaData) {
        fileMetaDataRepository.save(fileMetaData);
    }

    @Transactional
    public void create(UUID fileId, String fileName, FileCategory fileCategory) {
        FileMetaData fileMetaData = FileMetaData.builder()
            .fileId(fileId)
            .fileName(fileName)
            .fileCategory(fileCategory)
            .build();
        fileMetaDataRepository.save(fileMetaData);
    }

    public FileMetaData getByFileId(UUID fileId) {
        return fileMetaDataRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFound("FimeMetaData(fileId=" + fileId + "를 찾을 수 없습니다."));
    }

    public void delete(UUID originFileId) {
        fileMetaDataRepository.deleteById(originFileId);
    }
}
