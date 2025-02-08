package ddingdong.ddingdongBE.domain.form.service;

import ddingdong.ddingdongBE.common.exception.AuthenticationException.NonHaveAuthority;
import ddingdong.ddingdongBE.common.utils.TimeUtils;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.form.service.dto.command.CreateFormCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.command.CreateFormCommand.CreateFormFieldCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.command.UpdateFormCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.command.UpdateFormCommand.UpdateFormFieldCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormListQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.ApplicantStatisticQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.DepartmentStatisticQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.FieldStatisticsQuery;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacadeCentralFormServiceImpl implements FacadeCentralFormService {

    private final FormService formService;
    private final FormFieldService formFieldService;
    private final ClubService clubService;
    private final FormStatisticService formStatisTicService;

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

    @Transactional
    @Override
    public void deleteForm(Long formId, User user) {
        Club club = clubService.getByUserId(user.getId());
        Form form = formService.getById(formId);
        validateEqualsClub(club, form);
        formService.delete(form); //테이블 생성 시 외래 키에 cascade 설정하여 formField 삭제도 자동으로 됨.
    }

    @Override
    public List<FormListQuery> getAllMyForm(User user) {
        Club club = clubService.getByUserId(user.getId());
        List<Form> forms = formService.getAllByClub(club);
        return forms.stream()
                .map(this::buildFormListQuery)
                .toList();
    }

    @Override
    public FormQuery getForm(Long formId) {
        Form form = formService.getById(formId);
        List<FormField> formFields = formFieldService.findAllByForm(form);
        return FormQuery.of(form, formFields);
    }

    @Override
    public FormStatisticsQuery getStatisticsByForm(User user, Long formId) {
        Club club = clubService.getByUserId(user.getId());
        Form form = formService.getById(formId);
        int totalCount = formStatisTicService.getTotalApplicationCountByForm(form);
        List<DepartmentStatisticQuery> departmentStatisticQueries = formStatisTicService.createDepartmentStatistics(totalCount, form);
        List<ApplicantStatisticQuery> applicantStatisticQueries = formStatisTicService.createApplicationStatistics(club, form);
        FieldStatisticsQuery fieldStatisticsQuery = formStatisTicService.createFieldStatisticsByForm(form);

        return new FormStatisticsQuery(totalCount, departmentStatisticQueries, applicantStatisticQueries, fieldStatisticsQuery);
    }

    private FormListQuery buildFormListQuery(Form form) {
        boolean isActive = TimeUtils.isDateInRange(LocalDate.now(), form.getStartDate(), form.getEndDate());
        return FormListQuery.from(form, isActive);
    }

    private void validateEqualsClub(Club club, Form form) {
        if (!Objects.equals(club.getId(), form.getClub().getId())) {
            throw new NonHaveAuthority();
        }
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
