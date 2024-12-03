package ddingdong.ddingdongBE.domain.vodprocessing.repository;

import ddingdong.ddingdongBE.domain.vodprocessing.entity.VodProcessingJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VodProcessingJobRepository extends JpaRepository<VodProcessingJob, Long> {

}
