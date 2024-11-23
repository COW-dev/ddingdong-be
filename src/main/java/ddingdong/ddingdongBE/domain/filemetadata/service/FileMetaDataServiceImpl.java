package ddingdong.ddingdongBE.domain.filemetadata.service;

import static ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus.COUPLED;
import static ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus.DELETED;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.repository.FileMetaDataRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
        if (ids == null || ids.isEmpty()) {
            return;
        }
        List<UUID> fileMetaDataIds = toUUIDs(ids);
        List<FileMetaData> fileMetaDatas = fileMetaDataRepository.findByIdIn(fileMetaDataIds);
        if (ids.size() != fileMetaDatas.size()) {
            throw new ResourceNotFound("해당 FileMetaData(id: " + fileMetaDataIds + ")를 찾을 수 없습니다.");
        }
        fileMetaDatas.stream()
            .filter(FileMetaData::isPending)
            .forEach(fileMetaData -> {
                fileMetaData.updateCoupledEntityInfo(domainType, entityId);
                fileMetaData.updateStatus(COUPLED);
            });
    }

    @Transactional
    @Override
    public void updateStatusToCoupled(String id, DomainType domainType, Long entityId) {
        if (id == null) {
            return;
        }
        UUID fileMetaDataId = UUID.fromString(id);
        FileMetaData fileMetaData = findById(fileMetaDataId);
        fileMetaData.updateCoupledEntityInfo(domainType, entityId);
        fileMetaData.updateStatus(COUPLED);
    }

    @Transactional
    @Override
    public void update(String id, DomainType domainType, Long entityId) {
        if (isCoupled(id)) {
            return;
        }
        updateStatusToDelete(domainType, entityId);
        updateStatusToCoupled(id, domainType, entityId);
    }

    @Transactional
    @Override
    public void update(List<String> ids, DomainType domainType, Long entityId) {
        if (ids == null || ids.isEmpty()) {
            updateStatusToDelete(domainType, entityId);
            return;
        }
        deleteOldIds(ids, domainType, entityId); //ids에 포함된 id를 가진 fileMetaData외에 전부 제거
        List<String> newIds = getNewIds(ids); //기존 id가 아닌 새로운 id 반환
        updateStatusToCoupled(newIds, domainType, entityId);
    }

    @Transactional
    @Override
    public void updateStatusToDelete(DomainType domainType, Long entityId) {
        List<FileMetaData> fileMetaDatas = fileMetaDataRepository.findAllByDomainTypeAndEntityId(domainType, entityId);
        fileMetaDatas.forEach(fileMetaData -> {
            fileMetaData.updateStatus(DELETED);
        });
        entityManager.flush();
        fileMetaDataRepository.deleteAll(fileMetaDatas);
    }

    private List<String> getNewIds(List<String> ids) {
        List<FileMetaData> fileMetaDatas = fileMetaDataRepository.findByIdIn(toUUIDs(ids));
        return fileMetaDatas.stream()
            .filter(FileMetaData::isPending)
            .map(fileMetaData -> String.valueOf(fileMetaData.getId()))
            .toList();
    }

    private boolean isCoupled(String id) {
        if (id == null) {
            return false;
        }
        FileMetaData fileMetaData = findById(UUID.fromString(id));
        return fileMetaData.isCoupled();
    }

    private void deleteOldIds(List<String> ids, DomainType domainType, Long entityId) {
        List<FileMetaData> fileMetaDatas = fileMetaDataRepository.findAllByDomainTypeAndEntityId(domainType, entityId);
        List<FileMetaData> deleteTarget = fileMetaDatas.stream()
            .filter(fileMetaData -> !ids.contains(String.valueOf(fileMetaData.getId())))
            .toList();
        deleteTarget.forEach(target -> target.updateStatus(DELETED));
        entityManager.flush();
        fileMetaDataRepository.deleteAll(deleteTarget);
    }

    private FileMetaData findById(UUID id) {
        return fileMetaDataRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFound("해당 FileMetaData(id: " + id + ")를 찾을 수 없습니다."));
    }

    private List<UUID> toUUIDs(List<String> ids) {
        return ids.stream()
            .map(UUID::fromString)
            .toList();
    }
}
