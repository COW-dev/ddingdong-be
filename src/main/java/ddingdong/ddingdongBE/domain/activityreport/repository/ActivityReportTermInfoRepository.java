package ddingdong.ddingdongBE.domain.activityreport.repository;

import ddingdong.ddingdongBE.domain.activityreport.entity.ActivityReportTermInfo;
import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface ActivityReportTermInfoRepository extends JpaRepository<ActivityReportTermInfo, Long> {

    Optional<ActivityReportTermInfo> findFirstByOrderByStartDateAsc();

    List<ActivityReportTermInfo> findAllByStartDateBetweenOrderByTermAsc(LocalDate from, LocalDate to);

    boolean existsByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate endDate, LocalDate startDate);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<ActivityReportTermInfo> findWithWriteLockByStartDateBetween(LocalDate from, LocalDate to);

    @Lock(LockModeType.PESSIMISTIC_READ)
    List<ActivityReportTermInfo> findWithShareLockByTermAndStartDateBetween(int term, LocalDate from, LocalDate to);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<ActivityReportTermInfo> findWithWriteLockByTermAndStartDateBetween(int term, LocalDate from, LocalDate to);

}
