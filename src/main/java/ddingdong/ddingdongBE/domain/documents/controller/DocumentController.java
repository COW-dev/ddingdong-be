package ddingdong.ddingdongBE.domain.documents.controller;

import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.DOCUMENT;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.FILE;

import ddingdong.ddingdongBE.domain.documents.api.DocumentApi;
import ddingdong.ddingdongBE.domain.documents.controller.dto.response.DocumentResponse;
import ddingdong.ddingdongBE.domain.documents.controller.dto.response.DocumentListResponse;
import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.domain.documents.service.DocumentService;
import ddingdong.ddingdongBE.domain.fileinformation.service.FileInformationService;
import ddingdong.ddingdongBE.file.dto.FileResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DocumentController implements DocumentApi {

    private final DocumentService documentService;
    private final FileInformationService fileInformationService;

    public List<DocumentListResponse> getDocuments() {
        return documentService.getDocuments().stream()
                .map(DocumentListResponse::from)
                .toList();
    }

    public DocumentResponse getDocument(@PathVariable Long documentId) {
        Document document = documentService.getById(documentId);
        List<FileResponse> fileResponse = fileInformationService.getFileUrls(
                FILE.getFileType() + DOCUMENT.getFileDomain() + document.getId());
        return DocumentResponse.of(document, fileResponse);
    }
}
