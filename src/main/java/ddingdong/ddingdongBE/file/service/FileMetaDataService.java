package ddingdong.ddingdongBE.file.service;

import ddingdong.ddingdongBE.file.entity.FileMetaData;
import ddingdong.ddingdongBE.file.repository.FileMetaDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileMetaDataService {

    private final FileMetaDataRepository fileMetaDataRepository;

    public void create(FileMetaData fileMetaData) {
        fileMetaDataRepository.save(fileMetaData);
    }

}
