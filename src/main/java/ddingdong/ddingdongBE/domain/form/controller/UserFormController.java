package ddingdong.ddingdongBE.domain.form.controller;

import ddingdong.ddingdongBE.domain.form.api.UserFormApi;
import ddingdong.ddingdongBE.domain.form.controller.dto.response.FormSectionResponse;
import ddingdong.ddingdongBE.domain.form.controller.dto.response.UserFormResponse;
import ddingdong.ddingdongBE.domain.form.service.FacadeUserFormService;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormSectionQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.UserFormQuery;
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

  @Override
  public UserFormResponse getForm(Long formId, String section) {
    UserFormQuery query = facadeUserFormService.getUserForm(formId, section);
    return UserFormResponse.from(query);
  }
}
