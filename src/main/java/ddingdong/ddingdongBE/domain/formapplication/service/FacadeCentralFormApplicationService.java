package ddingdong.ddingdongBE.domain.formapplication.service;

import ddingdong.ddingdongBE.domain.formapplication.service.dto.query.MyFormApplicationPageQuery;
import ddingdong.ddingdongBE.domain.user.entity.User;

public interface FacadeCentralFormApplicationService {

    MyFormApplicationPageQuery getMyFormApplicationPage(Long formId, User user, int size, Long currentCursorId);

}
