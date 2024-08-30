package ddingdong.ddingdongBE.file.service;

import ddingdong.ddingdongBE.file.entity.FileMetaData;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacadeFileMetaDataService {

    private final FileMetaDataService fileMetaDataService;

    public FileMetaData getFileUrlWithMetaData(UUID fileId) {
        return fileMetaDataService.getByFileId(fileId);
    }

}
