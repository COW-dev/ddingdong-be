package ddingdong.ddingdongBE.domain.documents.controller;

import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.DOCUMENT;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.FILE;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.documents.api.AdminDocumentApi;
import ddingdong.ddingdongBE.domain.documents.controller.dto.request.CreateDocumentRequest;
import ddingdong.ddingdongBE.domain.documents.controller.dto.request.UpdateDocumentRequest;
import ddingdong.ddingdongBE.domain.documents.controller.dto.response.AdminDocumentResponse;
import ddingdong.ddingdongBE.domain.documents.controller.dto.response.AdminDocumentListResponse;
import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.domain.documents.service.DocumentService;
import ddingdong.ddingdongBE.domain.fileinformation.service.FileInformationService;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.file.dto.FileResponse;
import ddingdong.ddingdongBE.file.service.FileService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    public void createDocument(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @ModelAttribute CreateDocumentRequest createDocumentRequest,
            @RequestPart(name = "uploadFiles") List<MultipartFile> uploadFiles) {
        User admin = principalDetails.getUser();
        Long createdDocumentId = documentService.create(createDocumentRequest.toEntity(admin));
        fileService.uploadDownloadableFile(createdDocumentId, uploadFiles, FILE, DOCUMENT);
    }

    public List<AdminDocumentListResponse> getAdminDocuments() {
        return documentService.getDocuments().stream()
                .map(AdminDocumentListResponse::from)
                .toList();
    }

    public AdminDocumentResponse getAdminDocument(@PathVariable Long documentId) {
        Document document = documentService.getById(documentId);
        List<FileResponse> fileResponse = fileInformationService.getFileUrls(
                FILE.getFileType() + DOCUMENT.getFileDomain() + document.getId());
        return AdminDocumentResponse.of(document, fileResponse);
    }

    public void updateDocument(@PathVariable Long documentId,
                               @ModelAttribute UpdateDocumentRequest updateDocumentRequest,
                               @RequestPart(name = "uploadFiles", required = false) List<MultipartFile> uploadFiles) {
        Long updateDocumentId = documentService.update(documentId, updateDocumentRequest.toEntity());
        fileService.deleteFile(updateDocumentId, FILE, DOCUMENT);
        fileService.uploadDownloadableFile(updateDocumentId, uploadFiles, FILE, DOCUMENT);
    }

    public void deleteDocument(@PathVariable Long documentId) {
        fileService.deleteFile(documentId, FILE, DOCUMENT);
        documentService.delete(documentId);
    }
}
