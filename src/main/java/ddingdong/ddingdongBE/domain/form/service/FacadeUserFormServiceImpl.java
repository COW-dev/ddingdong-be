package ddingdong.ddingdongBE.domain.form.service;

import ddingdong.ddingdongBE.domain.form.controller.dto.request.CreateFormResponseRequest;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.service.dto.command.CreateFormResponseCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacadeUserFormServiceImpl implements FacadeUserFormService {

    private final FormResponseService formResponseService;
    private final FormAnswerService formAnswerService;

    @Override
    public void createFormResponse(CreateFormResponseCommand createFormResponseCommand) {

    }
}
