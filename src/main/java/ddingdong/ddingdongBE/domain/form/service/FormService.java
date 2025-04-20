package ddingdong.ddingdongBE.domain.form.service;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.Forms;
import java.time.LocalDate;
import java.util.List;

public interface FormService {

    Form create(Form form);

    Form getById(Long formId);

    void delete(Form form);

    Forms getAllByClub(Club club);

    List<Form> findOverlappingForms(Long id, LocalDate startDate, LocalDate endDate);

}
