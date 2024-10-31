package ddingdong.ddingdongBE.domain.documents.controller;

import ddingdong.ddingdongBE.domain.documents.api.DocumentApi;
import ddingdong.ddingdongBE.domain.documents.controller.dto.request.GetDocumentPagingRequest;
import ddingdong.ddingdongBE.domain.documents.controller.dto.response.DocumentListResponse;
import ddingdong.ddingdongBE.domain.documents.controller.dto.response.DocumentResponse;
import ddingdong.ddingdongBE.domain.documents.service.FacadeDocumentServiceImpl;
import ddingdong.ddingdongBE.domain.documents.service.dto.query.DocumentListPagingQuery;
import ddingdong.ddingdongBE.domain.documents.service.dto.query.DocumentQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DocumentController implements DocumentApi {

    private final FacadeDocumentServiceImpl facadeDocumentServiceImpl;

    @Override
    public DocumentListResponse getDocumentList(GetDocumentPagingRequest request) {
        DocumentListPagingQuery query = facadeDocumentServiceImpl.getDocumentList(request.toCommand());
        return DocumentListResponse.from(query);
    }

    @Override
    public DocumentResponse getDocument(@PathVariable Long documentId) {
        DocumentQuery query = facadeDocumentServiceImpl.getDocument(documentId);
        return DocumentResponse.from(query);
    }
}
