package ddingdong.ddingdongBE.domain.formapplication.controller;

import ddingdong.ddingdongBE.domain.formapplication.api.UserFormApplicationApi;
import ddingdong.ddingdongBE.domain.formapplication.controller.dto.request.CreateFormApplicationRequest;
import ddingdong.ddingdongBE.domain.formapplication.service.FacadeUserFormApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserFormApplicationController implements UserFormApplicationApi {

    private final FacadeUserFormApplicationService facadeUserFormApplicationService;

    @Override
    public void createFormApplication(Long formId,
            CreateFormApplicationRequest createFormApplicationRequest) {
        facadeUserFormApplicationService.createFormApplication(
                createFormApplicationRequest.toCommand(formId));
    }
}
