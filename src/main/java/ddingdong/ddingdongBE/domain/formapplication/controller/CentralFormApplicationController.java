package ddingdong.ddingdongBE.domain.formapplication.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.formapplication.controller.dto.request.UpdateFormApplicationStatusRequest;
import ddingdong.ddingdongBE.domain.formapplication.controller.dto.response.FormApplicationResponse;
import ddingdong.ddingdongBE.domain.formapplication.service.FacadeCentralFormApplicationService;
import ddingdong.ddingdongBE.domain.formapplication.api.CentralFormApplicationApi;
import ddingdong.ddingdongBE.domain.formapplication.controller.dto.response.MyAllFormApplicationsResponse;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.query.FormApplicationQuery;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.query.MyAllFormApplicationsQuery;
import ddingdong.ddingdongBE.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CentralFormApplicationController implements CentralFormApplicationApi {

    private final FacadeCentralFormApplicationService facadeCentralFormApplicationService;

    @Override
    public MyAllFormApplicationsResponse getAllFormApplication(Long formId,
            PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        MyAllFormApplicationsQuery query = facadeCentralFormApplicationService.getAllFormApplication(
                formId, user);
        return MyAllFormApplicationsResponse.from(query);
    }

    @Override
    public FormApplicationResponse getFormApplication(Long formId, Long applicationId,
            PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        FormApplicationQuery query = facadeCentralFormApplicationService.getFormApplication(formId,
                applicationId, user);
        return FormApplicationResponse.from(query);
    }

    @Override
    public void updateFormApplicationStatus(Long formId, PrincipalDetails principalDetails,
            UpdateFormApplicationStatusRequest request) {
        User user = principalDetails.getUser();
        facadeCentralFormApplicationService.updateStatus(request.toCommand(formId, user));
    }
}
