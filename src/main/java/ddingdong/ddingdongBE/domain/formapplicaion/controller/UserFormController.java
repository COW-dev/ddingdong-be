package ddingdong.ddingdongBE.domain.formapplicaion.controller;

import ddingdong.ddingdongBE.domain.formapplicaion.api.UserFormApi;
import ddingdong.ddingdongBE.domain.formapplicaion.controller.dto.request.CreateFormResponseRequest;
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
