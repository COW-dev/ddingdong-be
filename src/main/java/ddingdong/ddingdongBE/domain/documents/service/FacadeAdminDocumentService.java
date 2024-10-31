package ddingdong.ddingdongBE.domain.documents.service;

import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.domain.documents.service.dto.command.CreateDocumentCommand;
import ddingdong.ddingdongBE.domain.documents.service.dto.command.UpdateDocumentCommand;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.service.FacadeFileMetaDataService;
import ddingdong.ddingdongBE.domain.filemetadata.service.dto.command.UpdateAllFileMetaDataCommand;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacadeAdminDocumentService {

    private final DocumentService documentService;
    private final FileMetaDataService fileMetaDataService;

    @Transactional
    public void create(CreateDocumentCommand command) {
        Long documentId = documentService.create(command.toEntity());
        updateFileMetaDatas(command.fileIds(), DomainType.DOCUMENT_FILE, documentId);
    }

    @Transactional
    public void update(UpdateDocumentCommand command) {
        Document document = documentService.getById(command.documentId());
        documentService.update(document, command.toEntity());
        updateFileMetaDatas(command.fileIds(), DomainType.DOCUMENT_FILE, document.getId());
    }

    @Transactional
    public void delete(Long documentId) {
        documentService.delete(documentId);
    }

    private void updateFileMetaDatas(List<String> fileIds, DomainType domainType, Long id) {
        fileMetaDataService.updateAll(new UpdateAllFileMetaDataCommand(fileIds, domainType, id));
    }
}
