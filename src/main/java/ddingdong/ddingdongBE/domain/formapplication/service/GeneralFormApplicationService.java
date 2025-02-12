package ddingdong.ddingdongBE.domain.formapplication.service;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.repository.FormApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeneralFormApplicationService implements FormApplicationService {

    private final FormApplicationRepository formApplicationRepository;

    @Transactional
    @Override
    public FormApplication create(FormApplication formApplication) {
        return formApplicationRepository.save(formApplication);
    }

    @Override
    public List<FormApplication> getAllById(List<Long> applicationIds) {
        return formApplicationRepository.findAllById(applicationIds);
    }

    @Override
    public List<FormApplication> getAllFinalPassedByFormId(Long formId) {
        return formApplicationRepository.findAllFinalPassedByFormId(formId);
    }

    @Override
    public List<FormApplication> getAllByForm(Form form) {
        return formApplicationRepository.findAllByForm(form);
    }

    @Override
    public FormApplication getById(Long applicationId) {
        return formApplicationRepository.findById(applicationId)
                .orElseThrow(
                        () -> new ResourceNotFound("주어진 id로 해당 지원자를 찾을 수 없습니다.:" + applicationId));
    }
}
