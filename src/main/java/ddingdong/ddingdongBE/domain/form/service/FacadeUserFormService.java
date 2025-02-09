package ddingdong.ddingdongBE.domain.form.service;

import ddingdong.ddingdongBE.domain.form.service.dto.query.FormSectionQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.UserFormQuery;

public interface FacadeUserFormService {

  FormSectionQuery getFormSection(Long formId);

  UserFormQuery getUserForm(Long formId, String section);
}
