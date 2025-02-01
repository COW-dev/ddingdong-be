package ddingdong.ddingdongBE.domain.form.controller.dto;

import ddingdong.ddingdongBE.domain.form.api.UserFormApi;
import ddingdong.ddingdongBE.domain.form.controller.dto.request.CreateFormResponseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserFormController implements UserFormApi {

    @Override
    public void createFormResponse(
            CreateFormResponseRequest createFormResponseRequest
    ) {

    }
}
