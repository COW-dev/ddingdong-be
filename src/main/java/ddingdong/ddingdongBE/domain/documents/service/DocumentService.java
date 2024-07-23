package ddingdong.ddingdongBE.domain.documents.service;

import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.domain.documents.repository.DocumentRepository;
import java.util.List;
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

    public List<Document> findAll() {
        return documentRepository.findAll();
    }

}
