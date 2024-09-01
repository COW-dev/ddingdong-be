package ddingdong.ddingdongBE.domain.fixzone.repository;

import ddingdong.ddingdongBE.domain.fixzone.entity.FixZoneComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FixZoneCommentRepository extends JpaRepository<FixZoneComment, Long> {
}
