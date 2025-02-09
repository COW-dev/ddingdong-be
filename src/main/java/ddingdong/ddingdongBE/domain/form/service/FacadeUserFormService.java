package ddingdong.ddingdongBE.domain.form.service;

import ddingdong.ddingdongBE.domain.form.service.dto.query.FormSectionQuery;

public interface FacadeUserFormService {

  FormSectionQuery getFormSection(Long formId);
}
