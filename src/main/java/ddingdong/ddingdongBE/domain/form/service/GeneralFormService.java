package ddingdong.ddingdongBE.domain.form.service;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.Forms;
import ddingdong.ddingdongBE.domain.form.repository.FormRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeneralFormService implements FormService {

    private final FormRepository formRepository;

    @Transactional
    @Override
    @Caching(evict = {
            @CacheEvict(value = "clubsCache", allEntries = true)
    })
    public Form create(Form form) {
        return formRepository.save(form);
    }

    @Override
    public Form getById(Long formId) {
        return formRepository.findById(formId)
                .orElseThrow(() -> new ResourceNotFound("Form(formId=" + formId + ")를 찾을 수 없습니다."));
    }

    @Transactional
    @Override
    @Caching(evict = {
            @CacheEvict(value = "formsCache", key = "'form_' + #root.args[0].id + '_*'", allEntries = true),
            @CacheEvict(value = "formSectionsCache", key = "'form_' + #root.args[0].id + '_formSection'"),
            @CacheEvict(value = "clubsCache", allEntries = true)
    })
    public void delete(Form form) {
        formRepository.delete(form);
    }

    @Override
    public Forms getAllByClub(Club club) {
        return new Forms(formRepository.findAllByClub(club));
    }

    @Override
    public List<Form> findOverlappingForms(Long id, LocalDate startDate, LocalDate endDate) {
        return formRepository.findOverlappingForms(id, startDate, endDate);
    }

}
