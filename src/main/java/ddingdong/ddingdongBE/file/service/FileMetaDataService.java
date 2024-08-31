package ddingdong.ddingdongBE.file.service;

import com.github.f4b6a3.uuid.UuidCreator;
import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.file.controller.dto.response.FileUrlResponse;
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

    @Transactional
    public void delete(UUID fileId) {
        fileMetaDataRepository.deleteById(fileId);
    }

    public FileMetaData getByFileId(UUID fileId) {
        return fileMetaDataRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFound("FimeMetaData(fileId=" + fileId + "를 찾을 수 없습니다."));
    }

    //TODO : fileId를 못찾는 경우 예외 처리 필요
    public FileUrlResponse getFileUrlResponseByUrl(String fileUrl) {
        if (fileUrl == null) {
            return null;
        }
        String fileId = extractFileId(fileUrl);
        FileMetaData fileMetaData = getByFileId(UuidCreator.fromString(fileId));
        return FileUrlResponse.of(fileMetaData, fileUrl);
    }

    private String extractFileId(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }
}
