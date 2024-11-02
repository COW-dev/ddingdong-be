package ddingdong.ddingdongBE.domain.documents.service;

import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.domain.documents.service.dto.command.CreateDocumentCommand;
import ddingdong.ddingdongBE.domain.documents.service.dto.command.UpdateDocumentCommand;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacadeAdminDocumentServiceImpl implements FacadeAdminDocumentService {

    private final DocumentService documentService;
    private final FileMetaDataService fileMetaDataService;

    @Transactional
    @Override
    public void create(CreateDocumentCommand command) {
        Long documentId = documentService.create(command.toEntity());
        fileMetaDataService.updateToCoupled(command.fileIds(), DomainType.DOCUMENT_FILE, documentId);
    }

    @Transactional
    @Override
    public void update(UpdateDocumentCommand command) {
        Document document = documentService.getById(command.documentId());
        documentService.update(document, command.toEntity());
        fileMetaDataService.update(command.fileIds(), DomainType.DOCUMENT_FILE, command.documentId());
    }

    @Transactional
    @Override
    public void delete(Long documentId) {
        documentService.delete(documentId);
        fileMetaDataService.updateToDelete(DomainType.DOCUMENT_FILE, documentId);
    }
}
