package ddingdong.ddingdongBE.domain.form.controller;

import ddingdong.ddingdongBE.domain.form.api.UserFormApi;
import ddingdong.ddingdongBE.domain.form.controller.dto.response.FormSectionResponse;
import ddingdong.ddingdongBE.domain.form.service.FacadeUserFormService;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormSectionQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserFormController implements UserFormApi {

  private final FacadeUserFormService facadeUserFormService;

  @Override
  public FormSectionResponse getFormSections(Long formId) {
    FormSectionQuery query = facadeUserFormService.getFormSection(formId);
    return FormSectionResponse.from(query);
  }
}
