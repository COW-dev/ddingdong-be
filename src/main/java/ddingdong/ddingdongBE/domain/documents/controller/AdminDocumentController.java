package ddingdong.ddingdongBE.domain.documents.controller;

import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.DOCUMENT;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.FILE;

import ddingdong.ddingdongBE.domain.documents.api.AdminDocumentApi;
import ddingdong.ddingdongBE.domain.documents.controller.dto.request.GenerateDocumentRequest;
import ddingdong.ddingdongBE.domain.documents.controller.dto.request.ModifyDocumentRequest;
import ddingdong.ddingdongBE.domain.documents.controller.dto.response.AdminDetailDocumentResponse;
import ddingdong.ddingdongBE.domain.documents.controller.dto.response.AdminDocumentResponse;
import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.domain.documents.service.DocumentService;
import ddingdong.ddingdongBE.domain.fileinformation.service.FileInformationService;
import ddingdong.ddingdongBE.file.dto.FileResponse;
import ddingdong.ddingdongBE.file.service.FileService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class AdminDocumentController implements AdminDocumentApi {

    private final DocumentService documentService;
    private final FileService fileService;
    private final FileInformationService fileInformationService;

    public void generateDocument(@ModelAttribute GenerateDocumentRequest generateDocumentRequest,
                                 @RequestPart(name = "uploadFiles") List<MultipartFile> uploadFiles) {
        Long createdDocumentId = documentService.create(generateDocumentRequest.toEntity());
        fileService.uploadDownloadableFile(createdDocumentId, uploadFiles, FILE, DOCUMENT);
    }

    public List<AdminDocumentResponse> getAllDocuments() {
        return documentService.getAll().stream()
                .map(AdminDocumentResponse::from)
                .toList();
    }

    public AdminDetailDocumentResponse getDetailDocument(@PathVariable Long documentId) {
        Document document = documentService.getById(documentId);
        List<FileResponse> fileResponse = fileInformationService.getFileUrls(
                FILE.getFileType() + DOCUMENT.getFileDomain() + document.getId());
        return AdminDetailDocumentResponse.of(document, fileResponse);
    }

    public void modifyDocument(@PathVariable Long documentId,
                               @ModelAttribute ModifyDocumentRequest modifyDocumentRequest,
                               @RequestPart(name = "uploadFiles", required = false) List<MultipartFile> uploadFiles) {
        Long updateDocumentId = documentService.update(documentId, modifyDocumentRequest.toEntity());
        fileService.deleteFile(updateDocumentId, FILE, DOCUMENT);
        fileService.uploadDownloadableFile(updateDocumentId, uploadFiles, FILE, DOCUMENT);
    }

    public void deleteDocument(@PathVariable Long documentId) {
        fileService.deleteFile(documentId, FILE, DOCUMENT);
        documentService.delete(documentId);
    }
}
