package ddingdong.ddingdongBE.domain.formapplicaion.service;

import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.formapplicaion.entity.FormAnswer;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.formapplicaion.entity.FormApplication;
import ddingdong.ddingdongBE.domain.form.service.FormFieldService;
import ddingdong.ddingdongBE.domain.form.service.FormService;
import ddingdong.ddingdongBE.domain.formapplicaion.service.dto.CreateFormApplicationCommand;
import ddingdong.ddingdongBE.domain.formapplicaion.service.dto.CreateFormApplicationCommand.CreateFormAnswerCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacadeUserFormServiceImpl implements FacadeUserFormService {

    private final FormApplicationService formApplicationService;
    private final FormAnswerService formAnswerService;
    private final FormService formService;
    private final FormFieldService formFieldService;

    @Transactional
    @Override
    public void createFormApplication(Long formId, CreateFormApplicationCommand createFormApplicationCommand) {
        Form form = formService.getById(formId);
        FormApplication formApplication = createFormApplicationCommand.toEntity(form);
        FormApplication savedFormApplication = formApplicationService.create(formApplication);

        List<FormAnswer> formAnswers = toFormAnswers(savedFormApplication, createFormApplicationCommand.formAnswerCommands());
        formAnswerService.createAll(formAnswers);
    }

    private List<FormAnswer> toFormAnswers(FormApplication savedFormApplication, List<CreateFormAnswerCommand> createFormAnswerCommands) {
        return createFormAnswerCommands.stream()
                .map(formAnswerCommand -> {
                    FormField formField = formFieldService.getById(formAnswerCommand.fieldId());
                    return formAnswerCommand.toEntity(savedFormApplication, formField);
                })
                .toList();
    }
}
