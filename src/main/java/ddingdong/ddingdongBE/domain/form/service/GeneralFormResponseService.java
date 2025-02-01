package ddingdong.ddingdongBE.domain.form.service;

import ddingdong.ddingdongBE.domain.form.entity.FormResponse;
import ddingdong.ddingdongBE.domain.form.repository.FormResponseRepository;
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
