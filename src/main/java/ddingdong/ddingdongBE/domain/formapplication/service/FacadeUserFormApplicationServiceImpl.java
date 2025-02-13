package ddingdong.ddingdongBE.domain.formapplication.service;

import static ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType.FORM_FILE;

import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.service.FormFieldService;
import ddingdong.ddingdongBE.domain.form.service.FormService;
import ddingdong.ddingdongBE.domain.form.service.FormStatisticService;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormAnswer;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.command.CreateFormApplicationCommand;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.command.CreateFormApplicationCommand.CreateFormAnswerCommand;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.query.FormApplicationCountQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacadeUserFormApplicationServiceImpl implements FacadeUserFormApplicationService {

    private final FormApplicationService formApplicationService;
    private final FormAnswerService formAnswerService;
    private final FormService formService;
    private final FormFieldService formFieldService;
    private final FileMetaDataService fileMetaDataService;
    private final FormStatisticService formStatisticService;

    @Transactional
    @Override
    public void createFormApplication(CreateFormApplicationCommand createFormApplicationCommand) {
        Form form = formService.getById(createFormApplicationCommand.formId());
        FormApplication formApplication = createFormApplicationCommand.toEntity(form);
        FormApplication savedFormApplication = formApplicationService.create(formApplication);

        List<FormAnswer> formAnswers = toFormAnswers(savedFormApplication,
                createFormApplicationCommand.formAnswerCommands());
        updateFileMetaDataStatusToCoupled(formAnswers, form);
        formAnswerService.createAll(formAnswers);
    }

    @Override
    public FormApplicationCountQuery getFormApplicationCount(Long formId) {
        Form form = formService.getById(formId);
        return FormApplicationCountQuery.from(formStatisticService.getTotalApplicationCountByForm(form));
    }

    private void updateFileMetaDataStatusToCoupled(List<FormAnswer> formAnswers, Form form) {
        formAnswers.forEach(formAnswer -> {
            if (formAnswer.isFile()) {
                fileMetaDataService.updateStatusToCoupled(
                        formAnswer.getValue(),
                        FORM_FILE,
                        form.getId()
                );
            }
        });
    }

    private List<FormAnswer> toFormAnswers(
            FormApplication savedFormApplication,
            List<CreateFormAnswerCommand> createFormAnswerCommands
    ) {
        return createFormAnswerCommands.stream()
                .map(formAnswerCommand -> formAnswerCommand.toEntity(
                        savedFormApplication,
                        formFieldService.getById(formAnswerCommand.fieldId())
                ))
                .toList();
    }
}
