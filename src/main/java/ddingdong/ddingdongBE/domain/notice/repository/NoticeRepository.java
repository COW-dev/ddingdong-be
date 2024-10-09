package ddingdong.ddingdongBE.domain.notice.repository;

import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
