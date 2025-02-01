package ddingdong.ddingdongBE.domain.form.repository;

import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.form.entity.FormResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormResponseRepository extends JpaRepository<FormResponse, Long> {
}
