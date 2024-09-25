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
@Transactional
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;

    public Long create(Document document) {
        Document createdDocument = documentRepository.save(document);
        return createdDocument.getId();
    }

    @Transactional(readOnly = true)
    public List<Document> getDocuments() {
        return documentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Document getById(Long documentId) {
        return getDocument(documentId);
    }

    public Long update(Long documentId, Document updatedDocument) {
        Document document = getDocument(documentId);
        document.updateDocument(updatedDocument);
        return document.getId();
    }

    public void delete(Long documentId) {
        Document document = getDocument(documentId);
        documentRepository.delete(document);
    }

    private Document getDocument(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_DOCUMENT.getText()));
    }

}
