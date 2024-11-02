package ddingdong.ddingdongBE.domain.filemetadata.service;

import static ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus.COUPLED;
import static ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus.DELETED;

import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.repository.FileMetaDataRepository;
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

    @Override
    public void updateToCoupled(List<String> ids, DomainType domainType, Long entityId) {
        List<UUID> fileMetaDataId = toUUIDs(ids);
        List<FileMetaData> fileMetaDatas = fileMetaDataRepository.findByIdIn(fileMetaDataId);
        if (fileMetaDatas.isEmpty()) {
            log.warn("fileMetaData를 찾을 수 없습니다. requestIds={}", ids);
            return;
        }
        fileMetaDatas.forEach(fileMetaData -> {
            fileMetaData.updateCoupledEntityInfo(domainType, entityId);
            fileMetaData.updateStatus(COUPLED);
        });
    }

    private List<UUID> toUUIDs(List<String> ids) {
        return ids.stream()
            .map(UUID::fromString)
            .toList();
    }

    @Override
    public void updateToCoupled(String id, DomainType domainType, Long entityId) {
        UUID fileMetaDataId = UUID.fromString(id);
        FileMetaData fileMetaData = fileMetaDataRepository.findById(fileMetaDataId).orElse(null);
        if (fileMetaData == null) {
            log.warn("fileMetaData를 찾을 수 없습니다. requestId={}", id);
            return;
        }
        fileMetaData.updateCoupledEntityInfo(domainType, entityId);
        fileMetaData.updateStatus(COUPLED);
    }

    @Override
    public void update(String id, DomainType domainType, Long entityId) {
        updateToDelete(domainType, entityId);
        if (id == null) {
            log.info("업데이트 중 업로드 이미지가 제거되었습니다.");
            return;
        }
        updateToCoupled(id, domainType, entityId);
    }

    @Override
    public void update(List<String> ids, DomainType domainType, Long entityId) {
        updateToDelete(domainType, entityId);
        if (ids == null || ids.isEmpty()) {
            log.info("업데이트 중 업로드 이미지가 제거되었습니다.");
            return;
        }
        updateToCoupled(ids, domainType, entityId);
    }

    @Override
    public void updateToDelete(DomainType domainType, Long entityId) {
        List<FileMetaData> fileMetaDatas = getCoupledAllByDomainTypeAndEntityId(domainType, entityId);
        fileMetaDatas.forEach(fileMetaData -> {
            fileMetaData.updateStatus(DELETED);
            fileMetaDataRepository.delete(fileMetaData);
        });
    }
}
