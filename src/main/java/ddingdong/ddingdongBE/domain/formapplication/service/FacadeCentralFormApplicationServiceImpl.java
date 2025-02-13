package ddingdong.ddingdongBE.domain.formapplication.service;

import ddingdong.ddingdongBE.common.exception.AuthenticationException.NonHaveAuthority;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.service.FormService;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormAnswer;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.command.UpdateFormApplicationNoteCommand;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.command.UpdateFormApplicationStatusCommand;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.query.FormApplicationQuery;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.query.FormApplicationQuery.FormFieldAnswerListQuery;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.query.MyAllFormApplicationsQuery;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.query.MyAllFormApplicationsQuery.FormApplicationListQuery;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.file.service.S3FileService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacadeCentralFormApplicationServiceImpl implements
        FacadeCentralFormApplicationService {

    private final FormService formService;
    private final FormApplicationService formApplicationService;
    private final FormAnswerService formAnswerService;
    private final FileMetaDataService fileMetaDataService;
    private final S3FileService s3FileService;
    private final ClubService clubService;

    @Override
    public MyAllFormApplicationsQuery getAllFormApplication(Long formId, User user) {
        Club club = clubService.getByUserId(user.getId());
        Form form = formService.getById(formId);
        validateAuthority(club, form);
        List<FormApplication> formApplications = formApplicationService.getAllByForm(form);
        List<FormApplicationListQuery> formApplicationListQueries = formApplications.stream()
                .map(FormApplicationListQuery::of)
                .toList();

        return MyAllFormApplicationsQuery.of(form, formApplicationListQueries);
    }

    @Override
    public FormApplicationQuery getFormApplication(Long formId, Long applicationId, User user) {
        Club club = clubService.getByUserId(user.getId());
        Form form = formService.getById(formId);
        validateAuthority(club, form);
        FormApplication formApplication = formApplicationService.getById(applicationId);
        List<FormAnswer> formAnswers = formAnswerService.getAllByApplication(formApplication);
        List<FormFieldAnswerListQuery> formFieldAnswerListQueries = buildFormFieldAnswerQueries(formAnswers);
        return FormApplicationQuery.of(form, formApplication, formFieldAnswerListQueries);
    }

    @Transactional
    @Override
    public void updateStatus(UpdateFormApplicationStatusCommand command) {
        List<FormApplication> formApplications = formApplicationService.getAllById(
                command.applicationIds());
        formApplications.forEach(formApplication -> formApplication.updateStatus(command.status()));
    }

    @Transactional
    @Override
    public void updateNote(UpdateFormApplicationNoteCommand command) {
        FormApplication formApplication = formApplicationService.getById(command.applicationId());
        formApplication.updateNote(command.note());
    }

    private void validateAuthority(Club club, Form form) {
        if (!Objects.equals(club.getId(), form.getClub().getId())) {
            throw new NonHaveAuthority();
        }
    }

    private List<FormFieldAnswerListQuery> buildFormFieldAnswerQueries(List<FormAnswer> formAnswers) {
        return formAnswers.stream()
                .map(formAnswer -> {
                    if (formAnswer.isFile()) {
                        String fileId = formAnswer.getValue().get(0);
                        FileMetaData fileMetaData = fileMetaDataService.getById(fileId);
                        String cdnUrl = s3FileService.getUploadedFileUrl(fileMetaData.getFileKey()).cdnUrl();
                        return FormFieldAnswerListQuery.of(formAnswer, List.of(cdnUrl));
                    }
                    return FormFieldAnswerListQuery.from(formAnswer);
                })
                .toList();
    }
}
