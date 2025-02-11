package ddingdong.ddingdongBE.domain.formapplication.service;

import ddingdong.ddingdongBE.domain.formapplication.service.dto.command.CreateFormApplicationCommand;

public interface FacadeUserFormApplicationService {

    void createFormApplication(CreateFormApplicationCommand createFormApplicationCommand);

}
