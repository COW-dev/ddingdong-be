package ddingdong.ddingdongBE.domain.documents.controller;

import ddingdong.ddingdongBE.domain.documents.api.DocumentApi;
import ddingdong.ddingdongBE.domain.documents.controller.dto.response.DocumentListResponse;
import ddingdong.ddingdongBE.domain.documents.controller.dto.response.DocumentResponse;
import ddingdong.ddingdongBE.domain.documents.service.FacadeDocumentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DocumentController implements DocumentApi {

    private final FacadeDocumentService facadeDocumentService;

    public List<DocumentListResponse> getDocuments() {
        return facadeDocumentService.getDocuments();
    }

    public DocumentResponse getDocument(@PathVariable Long documentId) {
        return facadeDocumentService.getDocument(documentId);
    }
}
