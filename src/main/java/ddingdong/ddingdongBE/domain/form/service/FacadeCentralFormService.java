package ddingdong.ddingdongBE.domain.form.service;

import ddingdong.ddingdongBE.domain.form.service.dto.command.CreateFormCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.command.UpdateFormCommand;

public interface FacadeCentralFormService {

    void createForm(CreateFormCommand command);

    void updateForm(UpdateFormCommand command);
}
