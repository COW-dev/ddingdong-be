package ddingdong.ddingdongBE.domain.documents.service;

import ddingdong.ddingdongBE.domain.documents.entity.Document;
import java.util.List;

public interface DocumentService {

    Long create(Document document);

    List<Document> getDocumentListByPage(int page, int limit);

    Long getDocumentPageCount();

    Document getById(Long documentId);

    void update(Document document, Document updatedDocument);

    void delete(Long documentId);
}
