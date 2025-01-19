package ddingdong.ddingdongBE.domain.vodprocessing.repository;

import ddingdong.ddingdongBE.domain.vodprocessing.entity.VodProcessingNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VodProcessingNotificationRepository extends JpaRepository<VodProcessingNotification, Long> {

}
