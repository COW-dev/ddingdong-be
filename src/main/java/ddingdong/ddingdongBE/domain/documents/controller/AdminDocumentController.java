package ddingdong.ddingdongBE.domain.documents.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.documents.api.AdminDocumentApi;
import ddingdong.ddingdongBE.domain.documents.controller.dto.request.CreateDocumentRequest;
import ddingdong.ddingdongBE.domain.documents.controller.dto.request.UpdateDocumentRequest;
import ddingdong.ddingdongBE.domain.documents.service.FacadeAdminDocumentServiceImpl;
import ddingdong.ddingdongBE.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminDocumentController implements AdminDocumentApi {

    private final FacadeAdminDocumentServiceImpl facadeAdminDocumentServiceImpl;

    public void createDocument(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        CreateDocumentRequest createDocumentRequest
    ) {
        User admin = principalDetails.getUser();
        facadeAdminDocumentServiceImpl.create(createDocumentRequest.toCommand(admin));
    }

    public void updateDocument(
        @PathVariable Long documentId,
        UpdateDocumentRequest updateDocumentRequest
    ) {
        facadeAdminDocumentServiceImpl.update(updateDocumentRequest.toCommand(documentId));
    }

    public void deleteDocument(@PathVariable Long documentId) {
        facadeAdminDocumentServiceImpl.delete(documentId);
    }
}
