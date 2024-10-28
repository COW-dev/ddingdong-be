package ddingdong.ddingdongBE.domain.filemetadata.service;

import static ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus.ACTIVATED;
import static ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus.ATTACHED;

import com.github.f4b6a3.uuid.UuidCreator;
import ddingdong.ddingdongBE.domain.filemetadata.entity.EntityType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.service.dto.query.FileMetaDataListQuery;
import ddingdong.ddingdongBE.domain.filemetadata.service.dto.command.CreateFileMetaDataCommand;
import ddingdong.ddingdongBE.domain.filemetadata.service.dto.command.UpdateAllFileMetaDataCommand;
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
    public List<FileMetaDataListQuery> getAllByEntityTypeAndEntityId(EntityType entityType, Long entityId) {
        return fileMetaDataService.findActivatedAllByEntityTypeAndEntityId(entityType, entityId).stream()
                .map(FileMetaDataListQuery::from)
                .toList();
    }

    @Override
    @Transactional
    public void updateAll(UpdateAllFileMetaDataCommand command) {
        List<FileMetaData> fileMetaDataList =
                fileMetaDataService.findAllByEntityTypeAndEntityId(command.entityType(), command.entityId());
        Set<UUID> existingIds = fileMetaDataList.stream()
                .map(FileMetaData::getId)
                .collect(Collectors.toSet());
        Set<UUID> newIds = command.ids().stream()
                .map(UuidCreator::fromString)
                .collect(Collectors.toSet());

        // attach files
        fileMetaDataList.stream()
                .filter(fileMetaData -> !newIds.contains(fileMetaData.getId()))
                .forEach(fileMetaData -> fileMetaData.updateStatus(ATTACHED));

        // active files
        fileMetaDataService.getByIds(
                        newIds.stream()
                                .filter(id -> !existingIds.contains(id))
                                .toList())
                .forEach(fileMetaData -> {
                    fileMetaData.updateLinedEntityInfo(command.entityType(), command.entityId());
                    fileMetaData.updateStatus(ACTIVATED);
                });
    }

}
