package ddingdong.ddingdongBE.domain.form.service;

import static ddingdong.ddingdongBE.domain.club.entity.Position.MEMBER;

import ddingdong.ddingdongBE.common.exception.AuthenticationException.NonHaveAuthority;
import ddingdong.ddingdongBE.common.exception.InvalidatedMappingException.InvalidFormPeriodException;
import ddingdong.ddingdongBE.common.utils.TimeUtils;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.clubmember.entity.ClubMember;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.form.service.dto.command.CreateFormCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.command.CreateFormCommand.CreateFormFieldCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.command.SendApplicationResultEmailCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.command.UpdateFormCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.command.UpdateFormCommand.UpdateFormFieldCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormListQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.ApplicantStatisticQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.DepartmentStatisticQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.FieldStatisticsQuery;
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
    private final SesEmailService sesEmailService;

    @Transactional
    @Override
    public void createForm(CreateFormCommand createFormCommand) {
        Club club = clubService.getByUserId(createFormCommand.user().getId());
        validateDuplicationDate(club, createFormCommand.startDate(), createFormCommand.endDate());

        Form form = createFormCommand.toEntity(club);
        Form savedForm = formService.create(form);

        List<FormField> formFields = toCreateFormFields(savedForm, createFormCommand.formFieldCommands());
        formFieldService.createAll(formFields);
    }

    @Transactional
    @Override
    public void updateForm(UpdateFormCommand updateFormCommand) {
        Club club = clubService.getByUserId(updateFormCommand.user().getId());
        validateDuplicationDate(club, updateFormCommand.startDate(), updateFormCommand.endDate());

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
        int totalCount = formStatisticService.getTotalApplicationCountByForm(form);
        List<DepartmentStatisticQuery> departmentStatisticQueries = formStatisticService.createDepartmentStatistics(
                totalCount, form);
        List<ApplicantStatisticQuery> applicantStatisticQueries = formStatisticService.createApplicationStatistics(club,
                form);
        FieldStatisticsQuery fieldStatisticsQuery = formStatisticService.createFieldStatisticsByForm(form);

        return new FormStatisticsQuery(totalCount, departmentStatisticQueries, applicantStatisticQueries,
                fieldStatisticsQuery);
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
    public void sendApplicationResultEmail(SendApplicationResultEmailCommand command) {
        List<FormApplication> formApplications = formApplicationService.getAllByFormIdAndFormApplicationStatus(
                command.formId(),
                FormApplicationStatus.findStatus(command.target())
        );
        EmailContent emailContent = EmailContent.of(command.title(), command.message());
        CompletableFuture<Void> future = sesEmailService.sendBulkResultEmails(formApplications, emailContent);

        try {
            future.get(5, TimeUnit.MINUTES);  // 최대 5분 대기
        } catch (Exception e) {
            log.error("Failed to send bulk emails", e);
            throw new RuntimeException("이메일 전송 중 오류가 발생했습니다.", e);
        }
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

    public void validateDuplicationDate(Club club, LocalDate startDate, LocalDate endDate) {
        List<Form> overlappingForms = formService.findOverlappingForms(club.getId(), startDate, endDate);

        if (!overlappingForms.isEmpty()) {
            throw new InvalidFormPeriodException();
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
