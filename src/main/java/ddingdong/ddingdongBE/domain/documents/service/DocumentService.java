package ddingdong.ddingdongBE.domain.documents.service;

import static ddingdong.ddingdongBE.common.constant.PageConstant.DEFAULT_DOCUMENT_PAGE_SIZE;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.domain.documents.repository.DocumentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;

    @Transactional
    public Long create(Document document) {
        Document createdDocument = documentRepository.save(document);
        return createdDocument.getId();
    }

    public List<Document> getDocumentListByPage(int page, int limit) {
        int offset = (page - 1) * limit;
        return documentRepository.findAllByPage(limit, offset);
    }

    public int getNoticePageCount() {
        int totalCount = documentRepository.countBy();
        return (totalCount + DEFAULT_DOCUMENT_PAGE_SIZE - 1) / DEFAULT_DOCUMENT_PAGE_SIZE;
    }

    public Document getById(Long documentId) {
        return documentRepository.findById(documentId)
            .orElseThrow(() -> new ResourceNotFound("해당 Document(ID: " + documentId + ")" + "를 찾을 수 없습니다."));
    }

    @Transactional
    public void update(Document document, Document updatedDocument) {
        document.updateDocument(updatedDocument);
    }

    @Transactional
    public void delete(Long documentId) {
        Document document = getById(documentId);
        documentRepository.delete(document);
    }
}
