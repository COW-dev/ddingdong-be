package ddingdong.ddingdongBE.domain.documents.controller;

import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.DOCUMENT;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.FILE;

import ddingdong.ddingdongBE.domain.documents.api.DocumentApi;
import ddingdong.ddingdongBE.domain.documents.controller.dto.request.GetDocumentByPageRequest;
import ddingdong.ddingdongBE.domain.documents.controller.dto.response.DetailDocumentResponse;
import ddingdong.ddingdongBE.domain.documents.controller.dto.response.GetDocumentByPageResponse;
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

    public GetDocumentByPageResponse getAllDocuments(GetDocumentByPageRequest request) {
        List<Document> documents = documentService.getAllDocumentByPage(
            request.getPage(),
            request.getLimit()
        );

        return GetDocumentByPageResponse.from(documents);

    }

    public DetailDocumentResponse getDetailDocument(@PathVariable Long documentId) {
        Document document = documentService.getById(documentId);
        List<FileResponse> fileResponse = fileInformationService.getFileUrls(
                FILE.getFileType() + DOCUMENT.getFileDomain() + document.getId());
        return DetailDocumentResponse.of(document, fileResponse);
    }
}
