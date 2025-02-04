package ddingdong.ddingdongBE.domain.formapplication.controller;

import ddingdong.ddingdongBE.domain.formapplication.api.UserFormApi;
import ddingdong.ddingdongBE.domain.formapplication.controller.dto.request.CreateFormApplicationRequest;
import ddingdong.ddingdongBE.domain.formapplication.service.FacadeUserFormService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserFormController implements UserFormApi {

    private final FacadeUserFormService facadeUserFormService;

    @Override
    public void createFormResponse(Long formId, CreateFormApplicationRequest createFormApplicationRequest) {
        facadeUserFormService.createFormApplication(createFormApplicationRequest.toCommand(formId));
    }
}
