package ddingdong.ddingdongBE.domain.formapplication.service;

import static ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType.FORM_FILE;

import ddingdong.ddingdongBE.common.exception.FormException.FormPeriodException;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.service.FormFieldService;
import ddingdong.ddingdongBE.domain.form.service.FormService;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormAnswer;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.command.CreateFormApplicationCommand;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.command.CreateFormApplicationCommand.CreateFormAnswerCommand;
import java.time.LocalDate;
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

    @Transactional
    @Override
    public void createFormApplication(CreateFormApplicationCommand createFormApplicationCommand) {
        Form form = formService.getById(createFormApplicationCommand.formId());
        LocalDate now = LocalDate.now();
        validateFormPeriod(form.getStartDate(), form.getEndDate(), now);
        FormApplication formApplication = createFormApplicationCommand.toEntity(form);
        FormApplication savedFormApplication = formApplicationService.create(formApplication);

        List<FormAnswer> formAnswers = toFormAnswers(savedFormApplication,
                createFormApplicationCommand.formAnswerCommands());
        updateFileMetaDataStatusToCoupled(formAnswers, form);
        formAnswerService.createAll(formAnswers);
    }

    private void validateFormPeriod(LocalDate startDate,
            LocalDate endDate, LocalDate now) {
        if (startDate.isAfter(now) || endDate.isBefore(now)) {
            throw new FormPeriodException();
        }
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
