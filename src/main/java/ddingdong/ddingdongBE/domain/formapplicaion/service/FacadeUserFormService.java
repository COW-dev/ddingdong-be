package ddingdong.ddingdongBE.domain.formapplicaion.service;

import ddingdong.ddingdongBE.domain.formapplicaion.service.dto.command.CreateFormApplicationCommand;

public interface FacadeUserFormService {

    void createFormApplication(CreateFormApplicationCommand createFormApplicationCommand);

}
