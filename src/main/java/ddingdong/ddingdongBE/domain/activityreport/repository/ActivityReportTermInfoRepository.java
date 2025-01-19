package ddingdong.ddingdongBE.domain.activityreport.repository;

import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReportTermInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityReportTermInfoRepository extends JpaRepository<ActivityReportTermInfo, Long> {

    Optional<ActivityReportTermInfo> findFirstByOrderByStartDateAsc();

}
