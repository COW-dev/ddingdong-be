package ddingdong.ddingdongBE.domain.form.service;

import ddingdong.ddingdongBE.domain.form.entity.FormField;
import java.util.List;
import java.util.Optional;

public interface FormFieldService {

    void createAll(List<FormField> formFields);

    Optional<FormField> findById(Long id);

    FormField getById(Long id);

}
