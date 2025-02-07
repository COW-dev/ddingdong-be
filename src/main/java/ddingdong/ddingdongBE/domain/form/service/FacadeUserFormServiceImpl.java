package ddingdong.ddingdongBE.domain.form.service;

import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormSectionQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacadeUserFormServiceImpl implements FacadeUserFormService {

  private final FormService formService;

  @Override
  public FormSectionQuery getFormSection(Long formId) {
    Form form = formService.getById(formId);
    return FormSectionQuery.of(form.getTitle(), form.getDescription(), form.getSections());
  }
}
