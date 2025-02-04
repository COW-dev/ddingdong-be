package ddingdong.ddingdongBE.domain.formapplication.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.formapplication.service.FacadeCentralFormApplicationService;
import ddingdong.ddingdongBE.domain.formapplication.api.CentralFormApplicationApi;
import ddingdong.ddingdongBE.domain.formapplication.controller.dto.response.MyFormApplicationPageResponse;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.query.MyFormApplicationPageQuery;
import ddingdong.ddingdongBE.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CentralFormApplicationController implements CentralFormApplicationApi {

    private final FacadeCentralFormApplicationService facadeCentralFormService;

    @Override
    public MyFormApplicationPageResponse getMyFormApplicationPage(Long formId, int size, Long currentCursorId, PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        MyFormApplicationPageQuery query = facadeCentralFormService.getMyFormApplicationPage(formId, user, size, currentCursorId);
        return MyFormApplicationPageResponse.from(query);
    }
}
