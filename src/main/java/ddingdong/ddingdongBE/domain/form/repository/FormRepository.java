package ddingdong.ddingdongBE.domain.form.repository;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FormRepository extends JpaRepository<Form, Long> {

    List<Form> findAllByClub(Club club);

    @Query(value = "SELECT * FROM form f " +
            "WHERE f.club_id = :clubId " +
            "AND f.start_date <= :endDate " +
            "AND f.end_date >= :startDate",
            nativeQuery = true)
    List<Form> findOverlappingForms(
            @Param("clubId") Long clubId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
