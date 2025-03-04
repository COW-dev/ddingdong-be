package ddingdong.ddingdongBE.domain.activityreport.repository;

import ddingdong.ddingdongBE.domain.activityreport.entity.ActivityReport;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityReportRepository extends JpaRepository<ActivityReport, Long> {

    @Query(value = """
        select ac from ActivityReport ac
        join fetch ac.club c
        where YEAR(ac.createdAt) = :currentYear
        and ac.term = :term
        and c.deletedAt is null
        """)
    List<ActivityReport> findAllByCurrentYearAndTerm(@Param("currentYear") int currentYear, @Param("term") int term);

    List<ActivityReport> findByClubNameAndTerm(String clubName, String term);

    List<ActivityReport> findByClubName(String clubName);
}
