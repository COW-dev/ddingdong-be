package ddingdong.ddingdongBE.domain.filemetadata.service;

import static ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus.*;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.filemetadata.entity.EntityType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus;
import ddingdong.ddingdongBE.domain.filemetadata.repository.FileMetaDataRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileMetaDataServiceImpl implements FileMetaDataService {

    private final FileMetaDataRepository fileMetaDataRepository;

    @Override
    @Transactional
    public void save(List<FileMetaData> fileMetaDataList) {
        List<FileMetaData> newFileMetaDataList = fileMetaDataList.stream()
                .filter(fmd -> !fileMetaDataRepository.existsById(fmd.getId()))
                .toList();

        if (!newFileMetaDataList.isEmpty()) {
            fileMetaDataRepository.saveAll(newFileMetaDataList);
        }
    }

    @Override
    @Transactional
    public UUID save(FileMetaData fileMetaData) {
        if (fileMetaData == null || fileMetaDataRepository.existsById(fileMetaData.getId())) {
            return null;
        }
        FileMetaData savedFileMetaData = fileMetaDataRepository.save(fileMetaData);
        return savedFileMetaData.getId();
    }

    @Override
    public FileMetaData getById(UUID id) {
        return fileMetaDataRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("FimeMetaData(id=" + id + "를 찾을 수 없습니다."));
    }

    @Override
    public List<FileMetaData> findActivatedAllByEntityTypeAndEntityId(EntityType entityType, Long entityId) {
        return fileMetaDataRepository.findAllByEntityTypeAndEntityIdWithFileStatus(entityType, entityId, ACTIVATED);
    }

    @Override
    public List<FileMetaData> findAllByEntityTypeAndEntityId(EntityType entityType, Long entityId) {
        return fileMetaDataRepository.findAllByEntityTypeAndEntityId(entityType, entityId);
    }

    @Override
    public List<FileMetaData> getByIds(List<UUID> ids) {
        return fileMetaDataRepository.findByIdIn(ids);
    }

}
