package ddingdong.ddingdongBE.domain.calendar.repository;

import ddingdong.ddingdongBE.domain.calendar.entity.Event;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("""
            SELECT e FROM Event e LEFT JOIN FETCH e.category
            WHERE (e.repeatType = 'NONE'
                    AND e.startDate <= :lastDateOfMonth
                    AND e.endDate >= :firstDateOfMonth)
               OR (e.repeatType <> 'NONE'
                    AND e.startDate <= :lastDateOfMonth
                    AND e.repeatEndDate >= :firstDateOfMonth)
            """)
    List<Event> findAllByPeriod(
            @Param("firstDateOfMonth") LocalDate firstDateOfMonth,
            @Param("lastDateOfMonth") LocalDate lastDateOfMonth
    );
}
