package ddingdong.ddingdongBE.domain.form.service;

import ddingdong.ddingdongBE.common.exception.FormException.InvalidFieldTypeException;
import ddingdong.ddingdongBE.common.exception.FormException.InvalidFormEndDateException;
import ddingdong.ddingdongBE.common.exception.FormException.NonHaveFormAuthority;
import ddingdong.ddingdongBE.common.exception.FormException.OverlapFormPeriodException;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.clubmember.entity.ClubMember;
import ddingdong.ddingdongBE.domain.clubmember.service.ClubMemberService;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormEmailSendHistory;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.form.entity.FormResultSendingEmailInfo;
import ddingdong.ddingdongBE.domain.form.entity.Forms;
import ddingdong.ddingdongBE.domain.form.service.dto.command.CreateFormCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.command.CreateFormCommand.CreateFormFieldCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.command.SendApplicationResultEmailCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.command.UpdateFormCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.command.UpdateFormCommand.UpdateFormFieldCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.command.UpdateFormEndDateCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.query.EmailSendCountQuery;
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
import ddingdong.ddingdongBE.domain.form.service.event.SendFormResultEvent;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.service.FormAnswerService;
import ddingdong.ddingdongBE.domain.formapplication.service.FormApplicationService;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.email.entity.EmailContent;
import ddingdong.ddingdongBE.email.entity.EmailSendHistories;
import ddingdong.ddingdongBE.email.entity.EmailSendHistory;
import ddingdong.ddingdongBE.email.service.EmailSendHistoryService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationEventPublisher;
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
    private final FormAnswerService formAnswerService;
    private final FileMetaDataService fileMetaDataService;
    private final ClubMemberService clubMemberService;
    private final EmailSendHistoryService emailSendHistoryService;
    private final FormEmailSendHistoryService formEmailSendHistoryService;
    private final ApplicationEventPublisher applicationEventPublisher;

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
    @Caching(evict = {
            @CacheEvict(value = "formsCache", key = "'form_' + #root.args[0].formId() + '_*'", allEntries = true),
            @CacheEvict(value = "formSectionsCache", key = "'form_' + #root.args[0].formId() + '_formSection'"),
            @CacheEvict(value = "clubsCache", allEntries = true)
    })
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
        List<FileMetaData> fileMetaDatas = formAnswerService.getAllFileByForm(form);
        fileMetaDataService.updateStatusToDelete(fileMetaDatas);
        formService.delete(form); //테이블 생성 시 외래 키에 cascade 설정하여 formField 삭제도 자동으로 됨.
    }

    @Override
    public List<FormListQuery> getAllMyForm(User user) {
        Club club = clubService.getByUserId(user.getId());
        Forms forms = formService.getAllByClub(club);
        return forms.getForms().stream()
                .map(FormListQuery::from)
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
        validateEqualsClub(club, form);
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
    public void registerApplicantAsMember(Long userId, Long formId) {
        Club club = clubService.getByUserId(userId);
        List<ClubMember> originClubMembers = club.getClubMembers();
        clubMemberService.deleteAll(originClubMembers);

        List<FormApplication> finalPassedFormApplications = formApplicationService.getAllFinalPassedByFormId(formId);
        List<ClubMember> finalPassedClubMembers = finalPassedFormApplications.stream()
                .map(ClubMember::createFromFormApplication)
                .toList();
        club.addClubMembers(finalPassedClubMembers);
        clubMemberService.saveAll(finalPassedClubMembers);
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

    @Transactional
    @Override
    public void sendApplicationResultEmail(SendApplicationResultEmailCommand command) {
        Club club = clubService.getByUserId(command.userId());
        List<FormApplication> formApplications = formApplicationService.getAllByFormIdAndFormApplicationStatus(
                command.formId(),
                command.target()
        );
        Form form = formService.getById(command.formId());
        EmailContent emailContent = EmailContent.of(command.title(), command.message(), club);
        FormEmailSendHistory formEmailSendHistory = formEmailSendHistoryService.create(form, command.target(),
                command.message());

        List<FormResultSendingEmailInfo> formResultSendingEmailInfos = formApplications.stream()
                .map(application -> {
                    EmailSendHistory emailSendHistory = emailSendHistoryService.save(
                            EmailSendHistory.createPending(application, formEmailSendHistory));
                    return new FormResultSendingEmailInfo(emailSendHistory.getId(), application.getEmail(),
                            application.getName(), emailContent);
                })
                .toList();
        applicationEventPublisher.publishEvent(new SendFormResultEvent(formResultSendingEmailInfos));
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

    @Override
    public EmailSendCountQuery getEmailSendCountByFormEmailSendHistoryId(Long formEmailSendHistoryId) {
        FormEmailSendHistory formEmailSendHistory = formEmailSendHistoryService.getById(formEmailSendHistoryId);
        EmailSendHistories emailSendHistories = emailSendHistoryService.getAllByFormEmailSendHistoryId(
                formEmailSendHistory.getId());

        return new EmailSendCountQuery(
                emailSendHistories.getTotalCount(),
                emailSendHistories.getSuccessCount(),
                emailSendHistories.getFailCount()
        );
    }

    private void validateEqualsClub(Club club, Form form) {
        if (form.isNotEqualClubId(club.getId())) {
            throw new NonHaveFormAuthority();
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
        if (endDate.isBefore(startDate)) {
            throw new InvalidFormEndDateException();
        }
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
                .toList();
    }
}
