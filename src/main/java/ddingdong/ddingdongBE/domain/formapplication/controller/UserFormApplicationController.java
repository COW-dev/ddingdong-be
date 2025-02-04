package ddingdong.ddingdongBE.domain.formapplication.controller;

import ddingdong.ddingdongBE.domain.formapplication.api.UserFormApplicationApi;
import ddingdong.ddingdongBE.domain.formapplication.controller.dto.request.CreateFormApplicationRequest;
import ddingdong.ddingdongBE.domain.formapplication.service.FacadeUserFormService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserFormApplicationController implements UserFormApplicationApi {

    private final FacadeUserFormService facadeUserFormService;

    @Override
    public void createFormResponse(Long formId, CreateFormApplicationRequest createFormApplicationRequest) {
        facadeUserFormService.createFormApplication(createFormApplicationRequest.toCommand(formId));
    }
}
