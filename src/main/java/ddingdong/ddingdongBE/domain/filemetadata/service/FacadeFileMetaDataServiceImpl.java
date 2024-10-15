package ddingdong.ddingdongBE.domain.filemetadata.service;

import ddingdong.ddingdongBE.domain.filemetadata.service.dto.CreateFileMetaDataCommand;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacadeFileMetaDataServiceImpl implements FacadeFileMetaDataService {

    private final FileMetaDataService fileMetaDataService;

    @Override
    public void create(CreateFileMetaDataCommand command) {
        String fileId = extractFilename(command.key());
        fileMetaDataService.create(command.toEntity(UUID.fromString(fileId)));
    }

    private String extractFilename(String key) {
        String[] splitKey = key.split("/");
        return splitKey[splitKey.length - 1];
    }

}
