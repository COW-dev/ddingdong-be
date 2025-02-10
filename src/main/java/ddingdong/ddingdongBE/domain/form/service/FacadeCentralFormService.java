package ddingdong.ddingdongBE.domain.form.service;

import ddingdong.ddingdongBE.domain.form.service.dto.command.CreateFormCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.command.UpdateFormCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormListQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.MultipleFieldStatisticsQuery;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.util.List;

public interface FacadeCentralFormService {

    void createForm(CreateFormCommand command);

    void updateForm(UpdateFormCommand command);

    void deleteForm(Long formId, User user);

    List<FormListQuery> getAllMyForm(User user);

    FormQuery getForm(Long formId);

    FormStatisticsQuery getStatisticsByForm(User user, Long formId);

    MultipleFieldStatisticsQuery getMultipleFieldStatistics(Long fieldId);

    void registerApplicantAsMember(Long formId);
}
