package ddingdong.ddingdongBE.domain.filemetadata.service;

import static ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus.COUPLED;
import static ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus.DELETED;

import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.service.dto.command.CreateFileMetaDataCommand;
import ddingdong.ddingdongBE.domain.filemetadata.service.dto.command.UpdateAllFileMetaDataCommand;
import ddingdong.ddingdongBE.domain.filemetadata.service.dto.query.FileMetaDataListQuery;
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
public class FacadeFileMetaDataServiceImpl implements FacadeFileMetaDataService {

    private final FileMetaDataService fileMetaDataService;

    @Override
    @Transactional
    public UUID create(CreateFileMetaDataCommand command) {
        return fileMetaDataService.save(command.toEntity());
    }

    @Override
    public List<FileMetaDataListQuery> getAllByEntityTypeAndEntityId(DomainType domainType, Long entityId) {
        return fileMetaDataService.findActivatedAll(domainType, entityId).stream()
                .map(FileMetaDataListQuery::from)
                .toList();
    }

    @Override
    @Transactional
    public void updateAll(UpdateAllFileMetaDataCommand command) {
        List<FileMetaData> fileMetaDataList =
                fileMetaDataService.findAllByEntityTypeAndEntityId(command.domainType(), command.entityId());
        Set<UUID> existingIds = fileMetaDataList.stream()
                .map(FileMetaData::getId)
                .collect(Collectors.toSet());
        Set<UUID> newIds = command.ids().stream()
                .map(UUID::fromString)
                .collect(Collectors.toSet());

        // delete files
        fileMetaDataList.stream()
                .filter(fileMetaData -> !newIds.contains(fileMetaData.getId()))
                .forEach(fileMetaData -> fileMetaData.updateStatus(DELETED));

        // couple files
        fileMetaDataService.getByIds(
                        newIds.stream()
                                .filter(id -> !existingIds.contains(id))
                                .toList())
                .forEach(fileMetaData -> {
                    fileMetaData.updateCoupledEntityInfo(command.domainType(), command.entityId());
                    fileMetaData.updateStatus(COUPLED);
                });
    }

}
