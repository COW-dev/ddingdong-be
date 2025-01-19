package ddingdong.ddingdongBE.domain.documents.service;

import ddingdong.ddingdongBE.domain.documents.service.dto.command.GetDocumentListCommand;
import ddingdong.ddingdongBE.domain.documents.service.dto.query.DocumentListPagingQuery;
import ddingdong.ddingdongBE.domain.documents.service.dto.query.DocumentQuery;

public interface FacadeDocumentService {

    DocumentListPagingQuery getDocumentList(GetDocumentListCommand command);

    DocumentQuery getDocument(Long documentId);
}
