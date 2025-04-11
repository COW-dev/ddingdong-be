package ddingdong.ddingdongBE.domain.filemetadata.service;

import static ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus.COUPLED;
import static ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus.DELETED;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.repository.FileMetaDataRepository;
import ddingdong.ddingdongBE.domain.filemetadata.service.dto.FileMetaDataIdOrderDto;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
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

    @Transactional
    @Override
    public UUID create(FileMetaData fileMetaData) {
        FileMetaData savedFileMetaData = fileMetaDataRepository.save(fileMetaData);
        return savedFileMetaData.getId();
    }

    @Override
    public FileMetaData getById(String id) {
        UUID uuid = UUID.fromString(id);
        return fileMetaDataRepository.findById(uuid)
            .orElseThrow(() -> new ResourceNotFound("해당 FileMetaData(id: " + uuid + ")를 찾을 수 없습니다.)"));
    }

    @Override
    public List<FileMetaData> getAllByIds(List<String> ids) {
        List<UUID> uuids = toUUIDs(ids);
        return fileMetaDataRepository.findByIdIn(uuids);
    }

    @Override
    public List<FileMetaData> getCoupledAllByDomainTypeAndEntityId(DomainType domainType, Long entityId) {
        return fileMetaDataRepository.findAllByDomainTypeAndEntityIdWithFileStatus(domainType, entityId, COUPLED);
    }

    @Override
    public List<FileMetaData> getCoupledAllByEntityId(Long entityId) {
        return fileMetaDataRepository.findAllByEntityIdWithFileStatus(entityId, COUPLED);
    }

    @Override
    public List<FileMetaData> getCoupledAllByDomainTypeAndEntityIdOrderedAsc(DomainType domainType,
                                                                             Long entityId) {
        return fileMetaDataRepository.findAllByDomainTypeAndEntityIdWithFileStatusOrderedAsc(
                domainType, entityId, COUPLED);
    }

    @Override
    public List<FileMetaData> getCoupledAllByEntityIds(List<Long> entityIds) {
        return fileMetaDataRepository.findAllWithBannerByEntityIds(entityIds);
    }

    @Transactional
    @Override
    public void updateStatusToCoupled(List<String> ids, DomainType domainType, Long entityId) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        List<UUID> fileMetaDataIds = toUUIDs(ids);
        List<FileMetaData> fileMetaDataList = fileMetaDataRepository.findByIdIn(fileMetaDataIds);
        if (ids.size() != fileMetaDataList.size()) {
            throw new ResourceNotFound("해당 FileMetaData(id: " + fileMetaDataIds + ")를 찾을 수 없습니다.");
        }
        fileMetaDataList.stream()
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
    public void updateStatusToCoupledWithOrder(
            List<FileMetaDataIdOrderDto> fileMetaDataIdOrderDtos,
            DomainType domainType,
            Long entityId
    ) {
        if (fileMetaDataIdOrderDtos == null || fileMetaDataIdOrderDtos.isEmpty()) {
            return;
        }
        List<UUID> fileMetaDataIds = toUUIDs(fileMetaDataIdOrderDtos.stream()
                .map(FileMetaDataIdOrderDto::fileMetaDataId)
                .toList());
        List<FileMetaData> fileMetaDataList = fileMetaDataRepository.findByIdIn(fileMetaDataIds);
        if (fileMetaDataIdOrderDtos.size() != fileMetaDataList.size()) {
            throw new ResourceNotFound("해당 FileMetaData(id: " + fileMetaDataIds + ")를 찾을 수 없습니다.");
        }
        Map<UUID, Integer> orderMap = fileMetaDataIdOrderDtos.stream()
                .collect(Collectors.toMap(
                        dto -> UUID.fromString(dto.fileMetaDataId()),
                        FileMetaDataIdOrderDto::fileMetaDataOrder
                ));

        fileMetaDataList.forEach(fileMetaData -> {
            fileMetaData.updateOrder(orderMap.get(fileMetaData.getId()));
            if (fileMetaData.isPending()) {
                fileMetaData.updateCoupledEntityInfo(domainType, entityId);
                fileMetaData.updateStatus(COUPLED);
            }
        });
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
    public void updateWithOrder(List<FileMetaDataIdOrderDto> fileMetaDataIdOrderDtos, DomainType domainType,
                                Long entityId) {
        if (fileMetaDataIdOrderDtos == null || fileMetaDataIdOrderDtos.isEmpty()) {
            updateStatusToDelete(domainType, entityId);
            return;
        }
        List<String> ids = fileMetaDataIdOrderDtos.stream()
                .map(FileMetaDataIdOrderDto::fileMetaDataId)
                .toList();
        deleteOldIds(ids, domainType, entityId); //ids에 포함된 id를 가진 fileMetaData외에 전부 제거
        updateStatusToCoupledWithOrder(fileMetaDataIdOrderDtos, domainType, entityId);
    }

    @Transactional
    @Override
    public void updateStatusToDelete(DomainType domainType, Long entityId) {
        List<FileMetaData> fileMetaDatas = fileMetaDataRepository.findAllByDomainTypeAndEntityId(domainType, entityId);
        fileMetaDatas.forEach(fileMetaData -> fileMetaData.updateStatus(DELETED));
    }

    @Override
    public void updateStatusToDelete(List<FileMetaData> fileMetaDatas) {
        fileMetaDatas.forEach(fileMetaData -> fileMetaData.updateStatus(DELETED));
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
