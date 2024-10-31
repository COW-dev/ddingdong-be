package ddingdong.ddingdongBE.domain.filemetadata.service;

import static ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus.COUPLED;
import static ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus.DELETED;

import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.repository.FileMetaDataRepository;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
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
    public UUID create(FileMetaData fileMetaData) {
        FileMetaData savedFileMetaData = fileMetaDataRepository.save(fileMetaData);
        return savedFileMetaData.getId();
    }

    @Override
    public List<FileMetaData> getCoupledAllByDomainTypeAndEntityId(DomainType domainType, Long entityId) {
        return fileMetaDataRepository.findAllByDomainTypeAndEntityIdWithFileStatus(domainType, entityId, COUPLED);
    }

    @Override
    @Transactional
    public void updateAll(List<String> ids, DomainType domainType, Long entityId) {
        if(ids == null || ids.isEmpty()) {
            return;
        }
        List<FileMetaData> fileMetaDataList =
                fileMetaDataRepository.findAllByDomainTypeAndEntityIdWithFileStatus(domainType, entityId, COUPLED);
        Set<UUID> existingIds = fileMetaDataList.stream()
                .map(FileMetaData::getId)
                .collect(Collectors.toSet());
        Set<UUID> newIds = ids.stream()
                .map(UUID::fromString)
                .collect(Collectors.toSet());

        // delete files
        fileMetaDataList.stream()
                .filter(fileMetaData -> !newIds.contains(fileMetaData.getId()))
                .forEach(fileMetaData -> fileMetaData.updateStatus(DELETED));

        // couple files
        fileMetaDataRepository.findByIdIn(
                        newIds.stream()
                                .filter(id -> !existingIds.contains(id))
                                .toList())
                .forEach(fileMetaData -> {
                    fileMetaData.updateCoupledEntityInfo(domainType, entityId);
                    fileMetaData.updateStatus(COUPLED);
                });
    }

}
