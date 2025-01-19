package ddingdong.ddingdongBE.domain.documents.service;

import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.domain.documents.service.dto.command.GetDocumentListCommand;
import ddingdong.ddingdongBE.domain.documents.service.dto.query.DocumentListPagingQuery;
import ddingdong.ddingdongBE.domain.documents.service.dto.query.DocumentQuery;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.file.service.S3FileService;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlAndNameQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FacadeDocumentServiceImpl implements FacadeDocumentService {

    private final DocumentService documentService;
    private final FileMetaDataService fileMetaDataService;
    private final S3FileService s3FileService;

    @Override
    public DocumentListPagingQuery getDocumentList(GetDocumentListCommand command) {
        List<Document> documents = documentService.getDocumentListByPage(command.page(), command.limit());
        Long totalPageCount = documentService.getDocumentPageCount();
        return DocumentListPagingQuery.of(documents, totalPageCount);
    }

    @Override
    public DocumentQuery getDocument(Long documentId) {
        Document document = documentService.getById(documentId);
        List<UploadedFileUrlAndNameQuery> fileInfoQueries = getFileInfos(documentId);
        return DocumentQuery.of(document, fileInfoQueries);
    }

    private List<UploadedFileUrlAndNameQuery> getFileInfos(Long documentId) {
        List<FileMetaData> fileMetaDatas = fileMetaDataService.getCoupledAllByDomainTypeAndEntityId(
                DomainType.DOCUMENT_FILE, documentId);
        return fileMetaDatas.stream()
                .map(fileMetaData -> s3FileService.getUploadedFileUrlAndName(fileMetaData.getFileKey(), fileMetaData.getFileName()))
                .toList();
    }
}
