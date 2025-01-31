package ddingdong.ddingdongBE.domain.form.service;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.form.service.dto.command.CreateFormCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.command.CreateFormCommand.CreateFormFieldCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.command.UpdateFormCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.command.UpdateFormCommand.UpdateFormFieldCommand;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacadeCentralFormServiceImpl implements FacadeCentralFormService{

    private final FormService formService;
    private final FormFieldService formFieldService;
    private final ClubService clubService;

    @Transactional
    @Override
    public void createForm(CreateFormCommand createFormCommand) {
        Club club = clubService.getByUserId(createFormCommand.user().getId());
        Form form = createFormCommand.toEntity(club);
        Form savedForm = formService.create(form);

        List<FormField> formFields = toCreateFormFields(savedForm, createFormCommand.formFieldCommands());
        formFieldService.createAll(formFields);
    }

    @Transactional
    @Override
    public void updateForm(UpdateFormCommand updateFormCommand) {
        Form originform = formService.getById(updateFormCommand.formId());
        Form updateForm = updateFormCommand.toEntity();
        originform.update(updateForm);

        List<FormField> originFormFields = formFieldService.findAllByForm(originform);
        formFieldService.deleteAll(originFormFields);

        List<FormField> updateFormFields = toUpdateFormFields(originform, updateFormCommand.formFieldCommands());
        formFieldService.createAll(updateFormFields);
    }

    private List<FormField> toUpdateFormFields(Form originform, List<UpdateFormFieldCommand> updateFormFieldCommands) {
        return updateFormFieldCommands.stream()
                .map(formFieldCommand -> formFieldCommand.toEntity(originform))
                .toList();
    }

    private List<FormField> toCreateFormFields(Form savedForm, List<CreateFormFieldCommand> createFormFieldCommands) {
        return createFormFieldCommands.stream()
                .map(formFieldCommand -> formFieldCommand.toEntity(savedForm))
                .toList();
    }
}
