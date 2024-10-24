package ddingdong.ddingdongBE.domain.filemetadata.service;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.repository.FileMetaDataRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GeneralFileMetaDataService implements FileMetaDataService {

    private final FileMetaDataRepository fileMetaDataRepository;

    @Override
    @Transactional
    public void create(List<FileMetaData> fileMetaDataList) {
        List<FileMetaData> newFileMetaDataList = fileMetaDataList.stream()
                .filter(fmd -> !fileMetaDataRepository.existsById(fmd.getFileId()))
                .toList();

        if (!newFileMetaDataList.isEmpty()) {
            fileMetaDataRepository.saveAll(newFileMetaDataList);
        }
    }

    @Override
    @Transactional
    public void create(FileMetaData fileMetaData) {
        if (fileMetaData == null || fileMetaDataRepository.existsById(fileMetaData.getFileId())) {
            return;
        }
        fileMetaDataRepository.save(fileMetaData);
    }

    @Override
    public FileMetaData getByFileId(UUID fileId) {
        return fileMetaDataRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFound("FimeMetaData(fileId=" + fileId + "를 찾을 수 없습니다."));
    }

}
