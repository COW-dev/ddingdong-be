package ddingdong.ddingdongBE.domain.documents.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.documents.api.AdminDocumentApi;
import ddingdong.ddingdongBE.domain.documents.controller.dto.request.CreateDocumentRequest;
import ddingdong.ddingdongBE.domain.documents.controller.dto.request.UpdateDocumentRequest;
import ddingdong.ddingdongBE.domain.documents.service.FacadeAdminDocumentService;
import ddingdong.ddingdongBE.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminDocumentController implements AdminDocumentApi {

    private final FacadeAdminDocumentService facadeAdminDocumentService;

    public void createDocument(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @ModelAttribute CreateDocumentRequest createDocumentRequest
    ) {
        User admin = principalDetails.getUser();
        facadeAdminDocumentService.create(createDocumentRequest.toCommand(admin));
    }

    public void updateDocument(
        @PathVariable Long documentId,
        @ModelAttribute UpdateDocumentRequest updateDocumentRequest
    ) {
        facadeAdminDocumentService.update(updateDocumentRequest.toCommand(documentId));
    }

    public void deleteDocument(@PathVariable Long documentId) {
        facadeAdminDocumentService.delete(documentId);
    }
}
