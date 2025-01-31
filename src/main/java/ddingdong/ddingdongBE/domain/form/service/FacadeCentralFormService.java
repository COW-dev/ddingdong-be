package ddingdong.ddingdongBE.domain.form.service;

import ddingdong.ddingdongBE.domain.form.service.dto.command.CreateFormCommand;

public interface FacadeCentralFormService {

    void createForm(CreateFormCommand command);
}
