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
public class GeneralDocumentService implements DocumentService {

    private final DocumentRepository documentRepository;

    @Transactional
    @Override
    public Long create(Document document) {
        Document createdDocument = documentRepository.save(document);
        return createdDocument.getId();
    }

    @Override
    public List<Document> getDocumentListByPage(int page, int limit) {
        int offset = (page - 1) * limit;
        return documentRepository.findAllByPage(limit, offset);
    }

    @Override
    public Long getDocumentPageCount() {
        Long totalCount = documentRepository.count();
        return (totalCount + DEFAULT_DOCUMENT_PAGE_SIZE - 1) / DEFAULT_DOCUMENT_PAGE_SIZE;
    }

    @Override
    public Document getById(Long documentId) {
        return documentRepository.findById(documentId)
            .orElseThrow(() -> new ResourceNotFound("해당 Document(ID: " + documentId + ")" + "를 찾을 수 없습니다."));
    }

    @Transactional
    @Override
    public void update(Document document, Document updatedDocument) {
        document.updateDocument(updatedDocument);
    }

    @Transactional
    @Override
    public void delete(Long documentId) {
        Document document = getById(documentId);
        documentRepository.delete(document);
    }
}
