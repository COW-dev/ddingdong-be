package ddingdong.ddingdongBE.domain.formapplication.service;

import ddingdong.ddingdongBE.domain.formapplication.service.dto.command.UpdateFormApplicationStatusCommand;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.query.FormApplicationQuery;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.query.MyAllFormApplicationsQuery;
import ddingdong.ddingdongBE.domain.user.entity.User;

public interface FacadeCentralFormApplicationService {

    MyAllFormApplicationsQuery getAllFormApplication(Long formId, User user);

    FormApplicationQuery getFormApplication(Long formId, Long applicationId, User user);

    void updateStatus(UpdateFormApplicationStatusCommand command);
}
