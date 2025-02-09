package ddingdong.ddingdongBE.domain.form.service;

import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import java.util.List;

public interface FormFieldService {

  void createAll(List<FormField> formFields);

  FormField getById(Long id);

  List<FormField> findAllByForm(Form form);

  void deleteAll(List<FormField> originFormFields);

  List<FormField> getAllByFormAndSection(Form form, String section);
}
