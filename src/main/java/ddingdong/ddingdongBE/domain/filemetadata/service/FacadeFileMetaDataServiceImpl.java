package ddingdong.ddingdongBE.domain.filemetadata.service;

import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacadeFileMetaDataServiceImpl implements FacadeFileMetaDataService {

    private final FileMetaDataService fileMetaDataService;

    public FileMetaData getFileUrlWithMetaData(UUID fileId) {
        return fileMetaDataService.getByFileId(fileId);
    }

}
