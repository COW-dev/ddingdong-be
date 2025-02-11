package ddingdong.ddingdongBE.domain.formapplication.service;

import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.service.FormService;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormAnswer;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.command.UpdateFormApplicationStatusCommand;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.query.FormApplicationQuery;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.query.MyFormApplicationsQuery;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.query.MyFormApplicationsQuery.FormApplicationListQuery;
import ddingdong.ddingdongBE.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacadeCentralFormApplicationServiceImpl implements
        FacadeCentralFormApplicationService {

    private final FormService formService;
    private final FormApplicationService formApplicationService;
    private final FormAnswerService formAnswerService;

    @Override
    public MyFormApplicationsQuery getMyFormApplicationPage(Long formId, User user) {
        Form form = formService.getById(formId);
        List<FormApplication> formApplications = formApplicationService.getAllByForm(form);
        if (formApplications == null) {
            return MyFormApplicationsQuery.createEmpty(form);
        }
        List<FormApplicationListQuery> formApplicationListQueries = formApplications.stream()
                .map(FormApplicationListQuery::of)
                .toList();

        return MyFormApplicationsQuery.of(form, formApplicationListQueries);
    }

    @Override
    public FormApplicationQuery getFormApplication(Long formId, Long applicationId, User user) {
        Form form = formService.getById(formId);
        FormApplication formApplication = formApplicationService.getById(applicationId);
        List<FormAnswer> formAnswers = formAnswerService.getAllByApplication(formApplication);
        return FormApplicationQuery.of(form, formApplication, formAnswers);
    }

    @Transactional
    @Override
    public void updateStatus(UpdateFormApplicationStatusCommand command) {
        List<FormApplication> formApplications = formApplicationService.getAllById(
                command.applicationIds());
        formApplications.forEach(formApplication -> formApplication.updateStatus(command.status()));
    }
}
