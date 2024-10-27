package ddingdong.ddingdongBE.domain.documents.controller;

import ddingdong.ddingdongBE.domain.documents.api.DocumentApi;
import ddingdong.ddingdongBE.domain.documents.controller.dto.request.GetDocumentPagingRequest;
import ddingdong.ddingdongBE.domain.documents.controller.dto.response.DocumentListResponse;
import ddingdong.ddingdongBE.domain.documents.controller.dto.response.DocumentResponse;
import ddingdong.ddingdongBE.domain.documents.service.FacadeDocumentService;
import ddingdong.ddingdongBE.domain.documents.service.dto.query.DocumentListQuery;
import ddingdong.ddingdongBE.domain.documents.service.dto.query.DocumentQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DocumentController implements DocumentApi {

    private final FacadeDocumentService facadeDocumentService;

    @Override
    public List<DocumentListResponse> getDocumentList(GetDocumentPagingRequest request) {
        List<DocumentListQuery> queries = facadeDocumentService.getDocumentList(request.toCommand());
        return queries.stream()
            .map(DocumentListResponse::from)
            .toList();
    }

    @Override
    public DocumentResponse getDocument(@PathVariable Long documentId) {
        DocumentQuery query = facadeDocumentService.getDocument(documentId);
        return DocumentResponse.from(query);
    }
}
