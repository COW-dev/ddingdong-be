package ddingdong.ddingdongBE.domain.documents.service;

import static ddingdong.ddingdongBE.domain.filemetadata.entity.FileCategory.DOCUMENT_FILE;

import ddingdong.ddingdongBE.common.vo.FileInfo;
import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.domain.documents.service.dto.command.CreateDocumentCommand;
import ddingdong.ddingdongBE.domain.documents.service.dto.command.UpdateDocumentCommand;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacadeAdminDocumentService {

    private final DocumentService documentService;
    private final FileMetaDataService fileMetaDataService;

    @Transactional
    public void create(CreateDocumentCommand command) {
        documentService.create(command.toEntity());
        createFileMetaDatas(command.fileInfos());
    }

    @Transactional
    public void update(UpdateDocumentCommand command) {
        Document document = documentService.getById(command.documentId());
        documentService.update(document, command.toEntity());
        createFileMetaDatas(command.fileInfos());
    }

    @Transactional
    public void delete(Long documentId) {
        documentService.delete(documentId);
    }

    private void createFileMetaDatas(List<FileInfo> fileInfos) {
        if(fileInfos.isEmpty()) {
            return;
        }
        List<FileMetaData> fileMetaDatas = fileInfos.stream()
            .map(fileInfo -> FileMetaData.of(fileInfo.fileKey(), DOCUMENT_FILE))
            .toList();
        fileMetaDataService.save(fileMetaDatas);
    }
}
