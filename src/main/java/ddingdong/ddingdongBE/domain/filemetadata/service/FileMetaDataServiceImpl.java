package ddingdong.ddingdongBE.domain.filemetadata.service;

import static ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus.COUPLED;
import static ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus.DELETED;

import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.repository.FileMetaDataRepository;
import jakarta.persistence.EntityManager;
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
    private final EntityManager entityManager;

    @Override
    @Transactional
    public UUID create(FileMetaData fileMetaData) {
        FileMetaData savedFileMetaData = fileMetaDataRepository.save(fileMetaData);
        return savedFileMetaData.getId();
    }

    @Override
    public List<FileMetaData> getCoupledAllByDomainTypeAndEntityId(DomainType domainType, Long entityId) {
        return fileMetaDataRepository.findAllByDomainTypeAndEntityIdWithFileStatus(domainType, entityId, COUPLED);
    }

    @Transactional
    @Override
    public void updateStatusToCoupled(List<String> ids, DomainType domainType, Long entityId) {
        ids.forEach(id -> {
            updateStatusToCoupled(id, domainType, entityId);
        });
    }

    @Transactional
    @Override
    public void updateStatusToCoupled(String id, DomainType domainType, Long entityId) {
        UUID fileMetaDataId = UUID.fromString(id);
        FileMetaData fileMetaData = fileMetaDataRepository.findById(fileMetaDataId).orElse(null);
        if (fileMetaData == null) {
            return;
        }
        fileMetaData.updateCoupledEntityInfo(domainType, entityId);
        fileMetaData.updateStatus(COUPLED);
    }

    @Transactional
    @Override
    public void update(String id, DomainType domainType, Long entityId) {
        updateStatusToDelete(domainType, entityId);
        if (id == null) {
            return;
        }
        updateStatusToCoupled(id, domainType, entityId);
    }

    @Transactional
    @Override
    public void update(List<String> ids, DomainType domainType, Long entityId) {
        updateStatusToDelete(domainType, entityId);
        if (ids == null || ids.isEmpty()) {
            return;
        }
        updateStatusToCoupled(ids, domainType, entityId);
    }

    @Transactional
    @Override
    public void updateStatusToDelete(DomainType domainType, Long entityId) {
        List<FileMetaData> fileMetaDatas = getCoupledAllByDomainTypeAndEntityId(domainType, entityId);
        fileMetaDatas.forEach(fileMetaData -> {
            fileMetaData.updateStatus(DELETED);
            entityManager.flush();
            fileMetaDataRepository.delete(fileMetaData);
        });
    }

}
