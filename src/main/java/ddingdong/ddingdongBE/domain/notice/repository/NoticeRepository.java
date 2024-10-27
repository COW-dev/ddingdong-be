package ddingdong.ddingdongBE.domain.notice.repository;

import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Query(
        value = """
            SELECT *
            FROM notice AS n
            ORDER BY n.id DESC
            LIMIT :limit
            OFFSET :offsetValue
            """,
        nativeQuery = true
    )
    List<Notice> findAllByPage(int limit, int offsetValue);

    @Query(
        value = """
            SELECT COUNT(*)
            FROM Notice
            """
    )
    int countAll();

}
