package ddingdong.ddingdongBE.domain.documents.service;

import static ddingdong.ddingdongBE.common.exception.ErrorMessage.NO_SUCH_DOCUMENT;

import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.domain.documents.repository.DocumentRepository;
import java.util.List;
import java.util.NoSuchElementException;
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

    public List<Document> getAll() {
        return documentRepository.findAll();
    }

    @Transactional
    public Long update(Long documentId, Document updatedDocument) {
        Document document = getById(documentId);
        document.updateDocument(updatedDocument);
        return document.getId();
    }

    @Transactional
    public void delete(Long documentId) {
        Document document = getById(documentId);
        documentRepository.delete(document);
    }

    public Document getById(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_DOCUMENT.getText()));
    }

    public List<Document> getAllDocumentByPage(int page, int limit) {
        int offset = (page - 1) * limit;
        return documentRepository.findAllByPage(limit, offset);
    }

}
