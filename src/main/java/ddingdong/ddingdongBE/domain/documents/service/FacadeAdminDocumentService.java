package ddingdong.ddingdongBE.domain.documents.service;

import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.DOCUMENT;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.FILE;

import ddingdong.ddingdongBE.domain.documents.controller.dto.request.UpdateDocumentRequest;
import ddingdong.ddingdongBE.domain.documents.controller.dto.response.AdminDocumentListResponse;
import ddingdong.ddingdongBE.domain.documents.controller.dto.response.AdminDocumentResponse;
import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.domain.fileinformation.service.FileInformationService;
import ddingdong.ddingdongBE.file.dto.FileResponse;
import ddingdong.ddingdongBE.file.service.FileService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacadeAdminDocumentService {

    private final DocumentService documentService;
    private final FileService fileService;
    private final FileInformationService fileInformationService;


    @Transactional
    public void create(Document document, List<MultipartFile> uploadFiles) {
        Long createdDocumentId = documentService.create(document);
        fileService.uploadDownloadableFile(createdDocumentId, uploadFiles, FILE, DOCUMENT);
    }

    @Transactional
    public void update(Long documentId, UpdateDocumentRequest updateDocumentRequest,
        List<MultipartFile> uploadFiles) {
        documentService.update(documentId, updateDocumentRequest.toEntity());
        fileService.deleteFile(documentId, FILE, DOCUMENT);
        fileService.uploadDownloadableFile(documentId, uploadFiles, FILE, DOCUMENT);
    }

    @Transactional
    public void delete(Long documentId) {
        fileService.deleteFile(documentId, FILE, DOCUMENT);
        documentService.delete(documentId);
    }

    public List<AdminDocumentListResponse> getDocuments() {
        List<Document> documents = documentService.getDocuments();
        return documents.stream()
            .map(AdminDocumentListResponse::from)
            .toList();
    }

    public AdminDocumentResponse getDocument(Long documentId) {
        Document document = documentService.getById(documentId);
        List<FileResponse> fileResponses = fileInformationService.getFileUrls(
            FILE.getFileType() + DOCUMENT.getFileDomain() + document.getId());
        return AdminDocumentResponse.of(document, fileResponses);
    }
}
