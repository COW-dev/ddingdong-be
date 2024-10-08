package ddingdong.ddingdongBE.domain.documents.service;

import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.DOCUMENT;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.FILE;

import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.domain.documents.service.dto.command.CreateDocumentCommand;
import ddingdong.ddingdongBE.domain.documents.service.dto.command.UpdateDocumentCommand;
import ddingdong.ddingdongBE.domain.documents.service.dto.query.AdminDocumentListQuery;
import ddingdong.ddingdongBE.domain.documents.service.dto.query.AdminDocumentQuery;
import ddingdong.ddingdongBE.domain.fileinformation.service.FileInformationService;
import ddingdong.ddingdongBE.file.service.dto.FileResponse;
import ddingdong.ddingdongBE.file.service.FileService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacadeAdminDocumentService {

    private final DocumentService documentService;
    private final FileService fileService;
    private final FileInformationService fileInformationService;

    @Transactional
    public void create(CreateDocumentCommand command) {
        Long createdDocumentId = documentService.create(command.toEntity());
        fileService.uploadDownloadableFile(createdDocumentId, command.uploadFiles(), FILE, DOCUMENT);
    }

    @Transactional
    public void update(UpdateDocumentCommand command) {
        Long documentId = command.documentId();
        documentService.update(documentId, command.toEntity());
        fileService.deleteFile(documentId, FILE, DOCUMENT);
        fileService.uploadDownloadableFile(documentId, command.uploadFiles(), FILE, DOCUMENT);
    }

    @Transactional
    public void delete(Long documentId) {
        fileService.deleteFile(documentId, FILE, DOCUMENT);
        documentService.delete(documentId);
    }

    public List<AdminDocumentListQuery> getDocuments() {
        List<Document> documents = documentService.getDocuments();
        return documents.stream()
            .map(AdminDocumentListQuery::from)
            .toList();
    }

    public AdminDocumentQuery getDocument(Long documentId) {
        Document document = documentService.getById(documentId);
        List<FileResponse> fileResponses = fileInformationService.getFileUrls(
            FILE.getFileType() + DOCUMENT.getFileDomain() + document.getId());
        return AdminDocumentQuery.of(document, fileResponses);
    }
}
