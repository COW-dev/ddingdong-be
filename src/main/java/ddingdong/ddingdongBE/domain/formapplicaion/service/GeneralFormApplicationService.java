package ddingdong.ddingdongBE.domain.formapplicaion.service;

import ddingdong.ddingdongBE.domain.formapplicaion.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplicaion.repository.FormResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeneralFormApplicationService implements FormApplicationService {

    private final FormResponseRepository formResponseRepository;

    @Override
    public FormApplication create(FormApplication formApplication) {
        return formResponseRepository.save(formApplication);
    }

}
