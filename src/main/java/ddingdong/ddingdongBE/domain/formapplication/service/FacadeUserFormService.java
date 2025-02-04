package ddingdong.ddingdongBE.domain.formapplication.service;

import ddingdong.ddingdongBE.domain.formapplication.service.dto.command.CreateFormApplicationCommand;

public interface FacadeUserFormService {

    void createFormApplication(CreateFormApplicationCommand createFormApplicationCommand);

}
