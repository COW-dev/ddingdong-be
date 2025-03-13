package ddingdong.ddingdongBE.domain.form.service;

import static ddingdong.ddingdongBE.domain.club.entity.Position.MEMBER;

import ddingdong.ddingdongBE.common.exception.AuthenticationException.NonHaveAuthority;
import ddingdong.ddingdongBE.common.exception.FormException.InvalidFieldTypeException;
import ddingdong.ddingdongBE.common.exception.FormException.InvalidFormEndDateException;
import ddingdong.ddingdongBE.common.exception.FormException.OverlapFormPeriodException;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.clubmember.entity.ClubMember;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.form.entity.FormStatus;
import ddingdong.ddingdongBE.domain.form.service.dto.command.CreateFormCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.command.CreateFormCommand.CreateFormFieldCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.command.SendApplicationResultEmailCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.command.UpdateFormCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.command.UpdateFormCommand.UpdateFormFieldCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.command.UpdateFormEndDateCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormListQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.ApplicantStatisticQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.DepartmentStatisticQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.FieldStatisticsQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.MultipleFieldStatisticsQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.MultipleFieldStatisticsQuery.OptionStatisticQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.SingleFieldStatisticsQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.SingleFieldStatisticsQuery.SingleStatisticsQuery;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import ddingdong.ddingdongBE.domain.formapplication.service.FormApplicationService;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.email.SesEmailService;
import ddingdong.ddingdongBE.email.dto.EmailContent;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class FacadeCentralFormServiceImpl implements FacadeCentralFormService {

    private final FormService formService;
    private final FormFieldService formFieldService;
    private final ClubService clubService;
    private final FormStatisticService formStatisticService;
    private final FormApplicationService formApplicationService;
    private final FileMetaDataService fileMetaDataService;
    private final SesEmailService sesEmailService;

    @Transactional
    @Override
    public void createForm(CreateFormCommand createFormCommand) {
        Club club = clubService.getByUserId(createFormCommand.user().getId());
        validateDuplicationDate(club, createFormCommand.startDate(), createFormCommand.endDate());

        Form form = createFormCommand.toEntity(club);
        Form savedForm = formService.create(form);
        List<FormField> formFields = toCreateFormFields(savedForm,
                createFormCommand.formFieldCommands());
        formFieldService.createAll(formFields);
    }

    @Transactional
    @Override
    public void updateForm(UpdateFormCommand command) {
        Club club = clubService.getByUserId(command.user().getId());
        validateDuplicationDateExcludingSelf(club, command.startDate(), command.endDate(), command.formId());

        Form originform = formService.getById(command.formId());
        Form updateForm = command.toEntity();
        originform.update(updateForm);

        List<FormField> updatedFormFields = toUpdateFormFields(originform, command.formFieldCommands());
        originform.updateFormFields(updatedFormFields);
    }

    @Transactional
    @Override
    public void deleteForm(Long formId, User user) {
        Club club = clubService.getByUserId(user.getId());
        Form form = formService.getById(formId);
        validateEqualsClub(club, form);
        // TODO : fileMetaData의 formFile formAnswer 지우기
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
        int totalCount = formStatisticService.getTotalApplicationCountByForm(form);
        List<DepartmentStatisticQuery> departmentStatisticQueries = formStatisticService.createDepartmentStatistics(
                totalCount, form);
        List<ApplicantStatisticQuery> applicantStatisticQueries = formStatisticService.createApplicationStatistics(
                club, form);
        FieldStatisticsQuery fieldStatisticsQuery = formStatisticService.createFieldStatisticsByForm(
                form);

        return new FormStatisticsQuery(totalCount, departmentStatisticQueries,
                applicantStatisticQueries, fieldStatisticsQuery);
    }

    @Override
    public MultipleFieldStatisticsQuery getMultipleFieldStatistics(Long fieldId) {
        FormField formField = formFieldService.getById(fieldId);
        if (!formField.isMultipleChoice()) {
            throw new InvalidFieldTypeException();
        }
        String type = formField.getFieldType().name();
        List<OptionStatisticQuery> optionStatisticQueries = formStatisticService.createOptionStatistics(
                formField);
        return new MultipleFieldStatisticsQuery(type, optionStatisticQueries);
    }

    @Override
    @Transactional
    public void registerApplicantAsMember(Long formId) {
        List<FormApplication> finalPassedFormApplications = formApplicationService.getAllFinalPassedByFormId(formId);
        finalPassedFormApplications.forEach(formApplication -> {
            Club club = formApplication.getForm().getClub();
            ClubMember clubMember = ClubMember.builder()
                    .name(formApplication.getName())
                    .studentNumber(formApplication.getStudentNumber())
                    .department(formApplication.getDepartment())
                    .phoneNumber(formApplication.getPhoneNumber())
                    .position(MEMBER)
                    .build();
            club.addClubMember(clubMember);
        });
    }

    @Override
    public SingleFieldStatisticsQuery getTextFieldStatistics(Long fieldId) {
        FormField formField = formFieldService.getById(fieldId);
        if (!formField.isTextType()) {
            throw new InvalidFieldTypeException();
        }
        String type = formField.getFieldType().name();
        if (formField.isFile()) {
            List<SingleStatisticsQuery> textStatisticsQueries = formStatisticService.createFileStatistics(formField);
            return new SingleFieldStatisticsQuery(type, textStatisticsQueries);
        }
        List<SingleStatisticsQuery> textStatisticsQueries = formStatisticService.createTextStatistics(formField);
        return new SingleFieldStatisticsQuery(type, textStatisticsQueries);
    }

    @Override
    public void sendApplicationResultEmail(SendApplicationResultEmailCommand command) {
        Club club = clubService.getByUserId(command.userId());
        List<FormApplication> formApplications = formApplicationService.getAllByFormIdAndFormApplicationStatus(
                command.formId(),
                FormApplicationStatus.findStatus(command.target())
        );
        EmailContent emailContent = EmailContent.of(command.title(), command.message(), club);
        CompletableFuture<Void> future = sesEmailService.sendBulkResultEmails(formApplications, emailContent);

        try {
            future.get(5, TimeUnit.MINUTES);  // 최대 5분 대기
        } catch (Exception e) {
            log.error("Failed to send bulk emails", e);
            throw new RuntimeException("이메일 전송 중 오류가 발생했습니다.", e);
        }
    }

    @Transactional
    @Override
    public void updateFormEndDate(UpdateFormEndDateCommand command) {
        Club club = clubService.getByUserId(command.user().getId());
        Form form = formService.getById(command.formId());
        validateEndDate(form.getStartDate(), command.endDate());
        validateDuplicationDateExcludingSelf(club, form.getStartDate(), command.endDate(), command.formId());
        form.updateEndDate(command.endDate());
    }

    private FormListQuery buildFormListQuery(Form form) {
        FormStatus formStatus = FormStatus.getDescription(LocalDate.now(), form.getStartDate(),
                form.getEndDate());
        return FormListQuery.from(form, formStatus);
    }

    private void validateEqualsClub(Club club, Form form) {
        if (!Objects.equals(club.getId(), form.getClub().getId())) {
            throw new NonHaveAuthority();
        }
    }

    private void validateDuplicationDate(
            Club club,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<Form> overlappingForms = formService.findOverlappingForms(club.getId(), startDate,
                endDate);

        if (!overlappingForms.isEmpty()) {
            throw new OverlapFormPeriodException();
        }
    }

    private void validateDuplicationDateExcludingSelf(
            Club club,
            LocalDate startDate,
            LocalDate endDate,
            Long formId
    ) {
        List<Form> overlappingForms = formService.findOverlappingForms(club.getId(), startDate, endDate)
                .stream()
                .filter(form -> !form.isEqualsById(formId))
                .toList();

        if (!overlappingForms.isEmpty()) {
            throw new OverlapFormPeriodException();
        }
    }

    private void validateEndDate(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) { throw new InvalidFormEndDateException(); }
    }

    private List<FormField> toUpdateFormFields(Form originform,
            List<UpdateFormFieldCommand> updateFormFieldCommands) {
        return updateFormFieldCommands.stream()
                .map(formFieldCommand -> formFieldCommand.toEntity(originform))
                .toList();
    }

    private List<FormField> toCreateFormFields(Form savedForm,
            List<CreateFormFieldCommand> createFormFieldCommands) {
        return createFormFieldCommands.stream()
                .map(formFieldCommand -> formFieldCommand.toEntity(savedForm))
                .flatMap(formField -> formField.generateFormFieldsBySection(savedForm))
                .toList();
    }
}
