package ddingdong.ddingdongBE.domain.question.service;

import ddingdong.ddingdongBE.domain.question.service.dto.command.CreateQuestionCommand;
import ddingdong.ddingdongBE.domain.question.service.dto.command.UpdateQuestionCommand;
import ddingdong.ddingdongBE.domain.question.service.dto.query.AdminQuestionListQuery;
import java.util.List;

public interface FacadeAdminQuestionService {

    Long create(CreateQuestionCommand command);

    List<AdminQuestionListQuery> getAll();

    void update(UpdateQuestionCommand command);

    void delete(Long questionId);

}
