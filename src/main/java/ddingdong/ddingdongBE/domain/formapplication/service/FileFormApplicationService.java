package ddingdong.ddingdongBE.domain.formapplication.service;

import static ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType.FORM_FILE;
import static ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus.COUPLED;

import ddingdong.ddingdongBE.domain.formapplication.repository.FormApplicationRepository;
import ddingdong.ddingdongBE.domain.formapplication.repository.dto.FileApplicationInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileFormApplicationService {

    private final FormApplicationRepository formApplicationRepository;

    public List<FileApplicationInfo> getAllFileApplicationInfo(List<Long> applicationIds) {
        return formApplicationRepository.findAllFileApplicationInfo(FORM_FILE.name(), applicationIds, COUPLED.name());
    }
}
