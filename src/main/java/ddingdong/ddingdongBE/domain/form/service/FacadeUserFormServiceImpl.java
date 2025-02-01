package ddingdong.ddingdongBE.domain.form.service;

import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormAnswer;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.form.entity.FormResponse;
import ddingdong.ddingdongBE.domain.form.service.dto.command.CreateFormResponseCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacadeUserFormServiceImpl implements FacadeUserFormService {

    private final FormResponseService formResponseService;
    private final FormAnswerService formAnswerService;
    private final FormService formService;
    private final FormFieldService formFieldService;

    @Transactional
    @Override
    public void createFormResponse(CreateFormResponseCommand createFormResponseCommand) {
        Form form = formService.getById(createFormResponseCommand.form().getId());
        FormResponse formResponse = createFormResponseCommand.toEntity(form);
        FormResponse savedFormResponse = formResponseService.create(formResponse);

        List<FormAnswer> formAnswers = toFormAnswers(savedFormResponse, createFormResponseCommand.formAnswerCommands());
        formAnswerService.createAll(formAnswers);
    }

    private List<FormAnswer> toFormAnswers(FormResponse savedFormResponse, List<CreateFormResponseCommand.CreateFormAnswerCommand> createFormAnswerCommands) {
        return createFormAnswerCommands.stream()
                .map(formAnswerCommand -> {
                    FormField formField = formFieldService.findById(formAnswerCommand.fieldId())
                            .orElseThrow(() -> new IllegalArgumentException("해당 field를 id로 찾을 수 없습니다: " + formAnswerCommand.fieldId()));
                    return formAnswerCommand.toEntity(savedFormResponse, formField);
                })
                .toList();
    }
}
