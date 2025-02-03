package ddingdong.ddingdongBE.domain.form.service;

import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.form.repository.FormFieldRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeneralFormFieldService implements FormFieldService {

    private final FormFieldRepository formFieldRepository;

    @Transactional
    @Override
    public void createAll(List<FormField> formFields) {
        formFieldRepository.saveAll(formFields);
    }

    @Override
    public List<FormField> findAllByForm(Form form) {
        return formFieldRepository.findAllByForm(form);
    }

    @Transactional
    @Override
    public void deleteAll(List<FormField> originFormFields) {
        formFieldRepository.deleteAll(originFormFields);
    }
}
