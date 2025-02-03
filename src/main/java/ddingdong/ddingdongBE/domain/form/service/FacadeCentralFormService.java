package ddingdong.ddingdongBE.domain.form.service;

import ddingdong.ddingdongBE.domain.form.service.dto.command.CreateFormCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.command.UpdateFormCommand;
import ddingdong.ddingdongBE.domain.user.entity.User;

public interface FacadeCentralFormService {

    void createForm(CreateFormCommand command);

    void updateForm(UpdateFormCommand command);

    void deleteForm(Long formId, User user);
}
