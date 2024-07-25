package ddingdong.ddingdongBE.domain.documents.repository;

import ddingdong.ddingdongBE.domain.documents.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {

}
