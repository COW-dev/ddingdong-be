package ddingdong.ddingdongBE.domain.documents.repository;

import ddingdong.ddingdongBE.domain.documents.entity.Document;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query(
            value = """
            SELECT *
            FROM document AS d
            ORDER BY d.id DESC
            LIMIT :limit
            OFFSET :offset
            """,
            nativeQuery = true
    )
    List<Document> findAllByPage(int limit, int offset);
}
