package ddingdong.ddingdongBE.domain.form.service;

import ddingdong.ddingdongBE.common.exception.InvalidatedMappingException.InvalidFormPeriodException;
import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.repository.FormRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeneralFormService implements FormService{

    private final FormRepository formRepository;

    @Transactional
    @Override
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
    public void delete(Form form) {
        formRepository.delete(form);
    }

    @Override
    public List<Form> getAllByClub(Club club) {
        return formRepository.findAllByClub(club);
    }

    @Override
    public void validateDuplicationDate(Club club, LocalDate startDate, LocalDate endDate) {
        List<Form> overlappingForms = formRepository.findOverlappingForms(club.getId(), startDate, endDate);

        if (!overlappingForms.isEmpty()) {
            throw new InvalidFormPeriodException();
        }
    }
}
