package ddingdong.ddingdongBE.domain.form.service;

import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.repository.FormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeneralFormService implements FormService{

    private final FormRepository formRepository;

    @Transactional
    @Override
    public Form create(Form form) {
        return formRepository.save(form);
    }

    @Transactional
    @Override
    public Form getById(Long id) {
        return formRepository.getById(id);
    }
}
