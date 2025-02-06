package ddingdong.ddingdongBE.domain.form.service;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import java.time.LocalDate;
import java.util.List;

public interface FormService {

    Form create(Form form);

    Form getById(Long formId);

    void delete(Form form);

    List<Form> getAllByClub(Club club);

    void validateDuplicationDate(Club club, LocalDate startDate, LocalDate endDate);
}
