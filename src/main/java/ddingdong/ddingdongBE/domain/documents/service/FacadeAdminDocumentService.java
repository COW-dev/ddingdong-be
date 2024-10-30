package ddingdong.ddingdongBE.domain.documents.service;

import static ddingdong.ddingdongBE.domain.filemetadata.entity.FileCategory.DOCUMENT_FILE;

import ddingdong.ddingdongBE.common.vo.FileInfo;
import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.domain.documents.service.dto.command.CreateDocumentCommand;
import ddingdong.ddingdongBE.domain.documents.service.dto.command.UpdateDocumentCommand;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacadeAdminDocumentService {

    private final DocumentService documentService;
    private final FacadeFileMetaDataService facadeFileMetaDataService;

    @Transactional
    public void create(CreateDocumentCommand command) {
        Long documentId = documentService.create(command.toEntity());
        updateFileMetaDatas(command.fileIds(), DomainType.DOCUMENT_FILE, documentId);
    }

    @Transactional
    public void update(UpdateDocumentCommand command) {
        Document document = documentService.getById(command.documentId());
        documentService.update(document, command.toEntity());
        createFileMetaDatas(command.fileInfos());
    }

    @Transactional
    public void delete(Long documentId) {
        documentService.delete(documentId);
    }

    private void updateFileMetaDatas(List<String> fileIds, DomainType domainType, Long id) {
        facadeFileMetaDataService.updateAll(new UpdateAllFileMetaDataCommand(fileIds, domainType, id));
    }
}
