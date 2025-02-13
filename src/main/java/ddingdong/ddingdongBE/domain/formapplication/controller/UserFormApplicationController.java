package ddingdong.ddingdongBE.domain.formapplication.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.formapplication.api.UserFormApplicationApi;
import ddingdong.ddingdongBE.domain.formapplication.controller.dto.request.CreateFormApplicationRequest;
import ddingdong.ddingdongBE.domain.formapplication.controller.dto.response.FormApplicationCountResponse;
import ddingdong.ddingdongBE.domain.formapplication.service.FacadeUserFormApplicationService;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.query.FormApplicationCountQuery;
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

    @Override
    public FormApplicationCountResponse getNumberOfFormApplication(Long formId,
            PrincipalDetails principalDetails) {
        FormApplicationCountQuery query = facadeUserFormApplicationService.getFormApplicationCount(formId);
        return FormApplicationCountResponse.from(query);
    }
}
