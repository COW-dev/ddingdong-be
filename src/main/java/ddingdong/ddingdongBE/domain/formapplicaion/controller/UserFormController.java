package ddingdong.ddingdongBE.domain.formapplicaion.controller;

import ddingdong.ddingdongBE.domain.formapplicaion.api.UserFormApi;
import ddingdong.ddingdongBE.domain.formapplicaion.controller.dto.request.CreateFormApplicationRequest;
import ddingdong.ddingdongBE.domain.formapplicaion.service.FacadeUserFormService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserFormController implements UserFormApi {

    private final FacadeUserFormService facadeUserFormService;

    @Override
    public void createFormResponse(Long formId, CreateFormApplicationRequest createFormApplicationRequest) {
        facadeUserFormService.createFormApplication(formId, createFormApplicationRequest.toCommand());
    }
}
