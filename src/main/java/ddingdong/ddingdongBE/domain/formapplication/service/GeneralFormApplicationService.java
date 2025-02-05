package ddingdong.ddingdongBE.domain.formapplication.service;

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
    public Slice<FormApplication> getFormApplicationPageByFormId(Long formId, int size, Long currentCursorId) {
        Slice<FormApplication> formApplicationPages = formApplicationRepository.findPageByFormIdOrderById(formId, size + 1, currentCursorId);
        return buildSlice(formApplicationPages, size);
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
