package ddingdong.ddingdongBE.domain.form.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.form.api.CentralFormApi;
import ddingdong.ddingdongBE.domain.form.controller.dto.request.EmailResendApplicationResultRequest;
import ddingdong.ddingdongBE.domain.form.controller.dto.response.EmailSendStatusOverviewResponse;
import ddingdong.ddingdongBE.domain.form.controller.dto.response.EmailSendStatusResponse;
import ddingdong.ddingdongBE.domain.form.controller.dto.request.CreateFormRequest;
import ddingdong.ddingdongBE.domain.form.controller.dto.request.EmailSendApplicationResultRequest;
import ddingdong.ddingdongBE.domain.form.controller.dto.request.UpdateFormEndDateRequest;
import ddingdong.ddingdongBE.domain.form.controller.dto.request.UpdateFormRequest;
import ddingdong.ddingdongBE.domain.form.controller.dto.response.EmailSendCountResponse;
import ddingdong.ddingdongBE.domain.form.controller.dto.response.EmailSendRequestAcceptedResponse;
import ddingdong.ddingdongBE.domain.form.controller.dto.response.FormListResponse;
import ddingdong.ddingdongBE.domain.form.controller.dto.response.FormResponse;
import ddingdong.ddingdongBE.domain.form.controller.dto.response.FormStatisticsResponse;
import ddingdong.ddingdongBE.domain.form.controller.dto.response.MultipleFieldStatisticsResponse;
import ddingdong.ddingdongBE.domain.form.controller.dto.response.SingleFieldStatisticsResponse;
import ddingdong.ddingdongBE.domain.form.service.FacadeCentralFormService;
import ddingdong.ddingdongBE.domain.form.service.dto.query.EmailSendCountQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.EmailSendStatusOverviewQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.EmailSendStatusQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormListQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.MultipleFieldStatisticsQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.SingleFieldStatisticsQuery;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CentralFormController implements CentralFormApi {

    private final FacadeCentralFormService facadeCentralFormService;

    @Override
    public void createForm(
            CreateFormRequest createFormRequest,
            PrincipalDetails principalDetails
    ) {
        User user = principalDetails.getUser();
        facadeCentralFormService.createForm(createFormRequest.toCommand(user));
    }

    @Override
    public void updateForm(
            UpdateFormRequest updateFormRequest,
            Long formId,
            PrincipalDetails principalDetails
    ) {
        User user = principalDetails.getUser();
        facadeCentralFormService.updateForm(updateFormRequest.toCommand(user, formId));
    }

    @Override
    public void deleteForm(Long formId, PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        facadeCentralFormService.deleteForm(formId, user);
    }

    @Override
    public List<FormListResponse> getAllMyForm(PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        List<FormListQuery> queries = facadeCentralFormService.getAllMyForm(user);
        return queries.stream()
                .map(FormListResponse::from)
                .toList();
    }

    @Override
    public FormResponse getForm(Long formId) {
        FormQuery query = facadeCentralFormService.getForm(formId);
        return FormResponse.from(query);
    }

    @Override
    public FormStatisticsResponse getFormStatistics(
            Long formId,
            PrincipalDetails principalDetails
    ) {
        User user = principalDetails.getUser();
        FormStatisticsQuery query = facadeCentralFormService.getStatisticsByForm(user, formId);
        return FormStatisticsResponse.from(query);
    }

    @Override
    public MultipleFieldStatisticsResponse getMultipleFieldStatistics(Long fieldId) {
        MultipleFieldStatisticsQuery query = facadeCentralFormService.getMultipleFieldStatistics(
                fieldId);
        return MultipleFieldStatisticsResponse.from(query);
    }

    @Override
    public SingleFieldStatisticsResponse getTextFieldStatistics(Long fieldId) {
        SingleFieldStatisticsQuery query = facadeCentralFormService.getTextFieldStatistics(fieldId);
        return SingleFieldStatisticsResponse.from(query);
    }

    @Override
    public void registerMembers(PrincipalDetails principalDetails, Long formId) {
        Long userId = principalDetails.getUser().getId();
        facadeCentralFormService.registerApplicantAsMember(userId, formId);
    }

    @Override
    public EmailSendRequestAcceptedResponse sendApplicationResultEmail(
            Long formId,
            PrincipalDetails principalDetails,
            EmailSendApplicationResultRequest request
    ) {
        User user = principalDetails.getUser();
        Long formEmailSendHistoryId = facadeCentralFormService.sendApplicationResultEmail(
                request.toCommand(user.getId(), formId));
        return EmailSendRequestAcceptedResponse.from(formEmailSendHistoryId);
    }

    @Override
    public void updateFormEndDate(UpdateFormEndDateRequest updateFormEndDateRequest, Long formId,
            PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        facadeCentralFormService.updateFormEndDate(
                updateFormEndDateRequest.toCommand(user, formId));
    }

    @Override
    public EmailSendCountResponse getEmailSendCount(Long formEmailSendHistoryId) {
        EmailSendCountQuery query = facadeCentralFormService.getEmailSendCountByFormEmailSendHistoryId(
                formEmailSendHistoryId);
        return EmailSendCountResponse.from(query);
    }

    @Override
    public EmailSendStatusOverviewResponse getEmailSendStatusOverview(Long formId) {
        EmailSendStatusOverviewQuery query = facadeCentralFormService.getEmailSendStatusOverviewByFormId(
                formId);
        return EmailSendStatusOverviewResponse.from(query);
    }

    @Override
    public EmailSendStatusResponse getEmailSendStatus(Long formId, String status) {
        EmailSendStatusQuery query = facadeCentralFormService.getEmailSendStatusByFormIdAndFormApplicationStatus(
                formId, status);
        return EmailSendStatusResponse.from(query);
    }

    @Override
    public EmailSendRequestAcceptedResponse resendApplicationResultEmail(Long formId,
            EmailResendApplicationResultRequest request,
            PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        Long formEmailSendHistoryId = facadeCentralFormService.resendApplicationResultEmail(
                request.toCommand(user.getId(), formId));
        return EmailSendRequestAcceptedResponse.from(formEmailSendHistoryId);
    }
}
