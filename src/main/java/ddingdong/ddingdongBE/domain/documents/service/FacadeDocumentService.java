package ddingdong.ddingdongBE.domain.documents.service;

import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.domain.documents.service.dto.command.GetDocumentListCommand;
import ddingdong.ddingdongBE.domain.documents.service.dto.query.DocumentListQuery;
import ddingdong.ddingdongBE.domain.documents.service.dto.query.DocumentQuery;
import ddingdong.ddingdongBE.file.service.S3FileService;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlAndNameQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FacadeDocumentService {

    private final DocumentService documentService;
    private final S3FileService s3FileService;

    public List<DocumentListQuery> getDocumentList(GetDocumentListCommand command) {
        List<Document> documents = documentService.getDocumentListByPage(command.page(), command.limit());
        return documents.stream()
            .map(DocumentListQuery::from)
            .toList();
    }

    public DocumentQuery getDocument(Long documentId) {
        Document document = documentService.getById(documentId);
        List<UploadedFileUrlAndNameQuery> fileInfoQueries = document.getFileInfos().stream()
                .map(s3FileService::getUploadedFileUrlAndName)
                .toList();
        return DocumentQuery.of(document, fileInfoQueries);
    }
}
