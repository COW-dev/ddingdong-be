package ddingdong.ddingdongBE.domain.formapplication.service;

import ddingdong.ddingdongBE.domain.formapplication.service.dto.command.CreateFormApplicationCommand;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.query.FormApplicationCountQuery;

public interface FacadeUserFormApplicationService {

    void createFormApplication(CreateFormApplicationCommand createFormApplicationCommand);

    FormApplicationCountQuery getFormApplicationCount(Long formId);
}
