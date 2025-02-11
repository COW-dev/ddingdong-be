package ddingdong.ddingdongBE.domain.formapplication.service;

import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface FormApplicationService {

    FormApplication create(FormApplication formApplication);

    Slice<FormApplication> getFormApplicationPageByFormId(Long formId, int size, Long currentCursorId);

    FormApplication getById(Long applicationId);

    List<FormApplication> getAllById(List<Long> applicationIds);

    List<FormApplication> getAllFinalPassedByFormId(Long formId);

    List<FormApplication> getAllByFormIdAndFormApplicationStatus(Long formId, FormApplicationStatus status);
}
