package ddingdong.ddingdongBE.domain.formapplication.service;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.repository.FormApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
  public Slice<FormApplication> getFormApplicationPageByFormId(Long formId, int size,
      Long currentCursorId) {
    Slice<FormApplication> formApplicationPages = formApplicationRepository.findPageByFormIdOrderById(
        formId, size + 1, currentCursorId);
    return buildSlice(formApplicationPages, size);
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
    public FormApplication getById(Long applicationId) {
        return formApplicationRepository.findById(applicationId)
            .orElseThrow(() -> new ResourceNotFound("주어진 id로 해당 지원자를 찾을 수 없습니다.:"+applicationId));
    }

    private Slice<FormApplication> buildSlice(Slice<FormApplication> originalSlice, int size) {
        List<FormApplication> content = new ArrayList<>(originalSlice.getContent());
        if (content.isEmpty()) {
            return null;
        }

        boolean hasNext = content.size() > size;

        if (hasNext) {
            content.remove(content.size() - 1);
        }

        return new SliceImpl<>(content, PageRequest.of(originalSlice.getNumber(), size), hasNext);
    }
}
