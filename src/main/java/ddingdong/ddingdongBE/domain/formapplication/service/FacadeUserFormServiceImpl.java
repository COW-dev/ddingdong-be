package ddingdong.ddingdongBE.domain.formapplication.service;

import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.service.FormFieldService;
import ddingdong.ddingdongBE.domain.form.service.FormService;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormAnswer;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.command.CreateFormApplicationCommand;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.command.CreateFormApplicationCommand.CreateFormAnswerCommand;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  public void createFormApplication(CreateFormApplicationCommand createFormApplicationCommand) {
    Form form = formService.getById(createFormApplicationCommand.formId());
    FormApplication formApplication = createFormApplicationCommand.toEntity(form);
    FormApplication savedFormApplication = formApplicationService.create(formApplication);

    List<FormAnswer> formAnswers = toFormAnswers(savedFormApplication,
        createFormApplicationCommand.formAnswerCommands());
    formAnswerService.createAll(formAnswers);
  }

  private List<FormAnswer> toFormAnswers(
      FormApplication savedFormApplication,
      List<CreateFormAnswerCommand> createFormAnswerCommands
  ) {
    return createFormAnswerCommands.stream()
        .map(formAnswerCommand
            -> formAnswerCommand.toEntity(savedFormApplication,
            formFieldService.getById(formAnswerCommand.fieldId())))
        .toList();
  }
}
