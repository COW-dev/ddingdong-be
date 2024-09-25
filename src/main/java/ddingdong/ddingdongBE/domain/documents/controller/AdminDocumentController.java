package ddingdong.ddingdongBE.domain.documents.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.documents.api.AdminDocumentApi;
import ddingdong.ddingdongBE.domain.documents.controller.dto.request.CreateDocumentRequest;
import ddingdong.ddingdongBE.domain.documents.controller.dto.request.UpdateDocumentRequest;
import ddingdong.ddingdongBE.domain.documents.controller.dto.response.AdminDocumentListResponse;
import ddingdong.ddingdongBE.domain.documents.controller.dto.response.AdminDocumentResponse;
import ddingdong.ddingdongBE.domain.documents.service.FacadeAdminDocumentService;
import ddingdong.ddingdongBE.domain.user.entity.User;
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

    private final FacadeAdminDocumentService facadeAdminDocumentService;

    public void createDocument(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @ModelAttribute CreateDocumentRequest createDocumentRequest,
        @RequestPart(name = "uploadFiles") List<MultipartFile> uploadFiles
    ) {
        User admin = principalDetails.getUser();
        facadeAdminDocumentService.create(createDocumentRequest.toEntity(admin), uploadFiles);
    }

    public List<AdminDocumentListResponse> getAdminDocuments() {
        return facadeAdminDocumentService.getDocuments();
    }

    public AdminDocumentResponse getAdminDocument(@PathVariable Long documentId) {
        return facadeAdminDocumentService.getDocument(documentId);
    }

    public void updateDocument(
        @PathVariable Long documentId,
        @ModelAttribute UpdateDocumentRequest updateDocumentRequest,
        @RequestPart(name = "uploadFiles", required = false) List<MultipartFile> uploadFiles
    ) {
        facadeAdminDocumentService.update(documentId, updateDocumentRequest, uploadFiles);
    }

    public void deleteDocument(@PathVariable Long documentId) {
        facadeAdminDocumentService.delete(documentId);
    }
}
