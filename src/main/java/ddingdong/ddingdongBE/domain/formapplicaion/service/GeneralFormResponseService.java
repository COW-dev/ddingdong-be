package ddingdong.ddingdongBE.domain.formapplicaion.service;

import ddingdong.ddingdongBE.domain.formapplicaion.entity.FormResponse;
import ddingdong.ddingdongBE.domain.formapplicaion.repository.FormResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeneralFormResponseService implements FormResponseService {

    private final FormResponseRepository formResponseRepository;

    @Override
    public FormResponse create(FormResponse formResponse) {
        return formResponseRepository.save(formResponse);
    }

}
