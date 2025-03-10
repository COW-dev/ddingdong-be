package ddingdong.ddingdongBE.domain.formapplication.service;

import static ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType.FORM_FILE;
import static ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus.COUPLED;

import ddingdong.ddingdongBE.domain.formapplication.repository.FormAnswerRepository;
import ddingdong.ddingdongBE.domain.formapplication.repository.dto.FileAnswerInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileFormAnswerService {

    private final FormAnswerRepository formAnswerRepository;

    public List<FileAnswerInfo> getAllFileApplicationInfo(List<Long> answerIds) {
        return formAnswerRepository.findAllFileAnswerInfo(FORM_FILE.name(), answerIds, COUPLED.name());
    }
}
