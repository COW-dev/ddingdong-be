package ddingdong.ddingdongBE.domain.activityreport.repository;

import ddingdong.ddingdongBE.domain.activityreport.entity.ActivityReportTermInfo;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityReportTermInfoRepository extends JpaRepository<ActivityReportTermInfo, Long> {

    Optional<ActivityReportTermInfo> findFirstByOrderByStartDateAsc();

    List<ActivityReportTermInfo> findAllByStartDateBetweenOrderByTermAsc(LocalDate from, LocalDate to);

    boolean existsByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate endDate, LocalDate startDate);

}
