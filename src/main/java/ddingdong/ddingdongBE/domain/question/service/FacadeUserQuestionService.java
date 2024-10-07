package ddingdong.ddingdongBE.domain.question.service;

import ddingdong.ddingdongBE.domain.question.service.dto.query.AdminQuestionListQuery;
import ddingdong.ddingdongBE.domain.question.service.dto.query.UserQuestionListQuery;
import java.util.List;

public interface FacadeUserQuestionService {

    List<UserQuestionListQuery> getAll();



}
