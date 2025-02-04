package ddingdong.ddingdongBE.domain.form.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.form.api.CentralFormApi;
import ddingdong.ddingdongBE.domain.form.controller.dto.request.CreateFormRequest;
import ddingdong.ddingdongBE.domain.form.controller.dto.request.UpdateFormRequest;
import ddingdong.ddingdongBE.domain.form.controller.dto.response.FormListResponse;
import ddingdong.ddingdongBE.domain.form.controller.dto.response.FormResponse;
import ddingdong.ddingdongBE.domain.form.service.FacadeCentralFormService;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormListQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormQuery;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CentralFormController implements CentralFormApi {

    private final FacadeCentralFormService facadeCentralFormService;

    @Override
    public void createForm(
            CreateFormRequest createFormRequest,
            PrincipalDetails principalDetails
    ) {
        User user = principalDetails.getUser();
        facadeCentralFormService.createForm(createFormRequest.toCommand(user));
    }

    @Override
    public void updateForm(
            UpdateFormRequest updateFormRequest,
            Long formId,
            PrincipalDetails principalDetails
    ) {
        facadeCentralFormService.updateForm(updateFormRequest.toCommand(formId));
    }

    @Override
    public void deleteForm(Long formId, PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        facadeCentralFormService.deleteForm(formId, user);
    }

    @Override
    public List<FormListResponse> getAllMyForm(PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        List<FormListQuery> queries = facadeCentralFormService.getAllMyForm(user);
        return queries.stream()
                .map(FormListResponse::from)
                .toList();
    }

    @Override
    public FormResponse getForm(Long formId) {
        FormQuery query = facadeCentralFormService.getForm(formId);
        return FormResponse.from(query);
    }
}
