package ddingdong.ddingdongBE.domain.activityreport.repository;

import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReport;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityReportRepository extends JpaRepository<ActivityReport, Long> {
    List<ActivityReport> findByClubNameAndTerm(String clubName, String term);

    List<ActivityReport> findByClubName(String clubName);

    @Modifying
    @Query("DELETE FROM ActivityReport a WHERE a IN :activityReports")
    void deleteAllInBatch(@Param("activityReports") List<ActivityReport> activityReports);
}
