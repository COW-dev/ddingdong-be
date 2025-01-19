package ddingdong.ddingdongBE.domain.documents.service;

import ddingdong.ddingdongBE.domain.documents.service.dto.command.CreateDocumentCommand;
import ddingdong.ddingdongBE.domain.documents.service.dto.command.UpdateDocumentCommand;

public interface FacadeAdminDocumentService {

   void create(CreateDocumentCommand command);

   void update(UpdateDocumentCommand command);

    void delete(Long documentId);
}
