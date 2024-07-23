package ddingdong.ddingdongBE.domain.documents.controller;

import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.DOCUMENT;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.FILE;

import ddingdong.ddingdongBE.domain.documents.controller.dto.request.GenerateDocumentRequest;
import ddingdong.ddingdongBE.domain.documents.controller.dto.response.AdminDocumentResponse;
import ddingdong.ddingdongBE.domain.documents.service.DocumentService;
import ddingdong.ddingdongBE.file.service.FileService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/server/admin/documents")
@RequiredArgsConstructor
public class AdminDocumentController {

    private final DocumentService documentService;
    private final FileService fileService;

    @PostMapping
    public void generate(@ModelAttribute GenerateDocumentRequest generateDocumentRequest,
                         @RequestPart(name = "uploadFiles") List<MultipartFile> uploadFiles) {
        Long createdDocumentId = documentService.create(generateDocumentRequest.toEntity());
        fileService.uploadDownloadableFile(createdDocumentId, uploadFiles, FILE, DOCUMENT);
    }

    @GetMapping
    public List<AdminDocumentResponse> getAll() {
        return documentService.findAll().stream()
                .map(AdminDocumentResponse::from)
                .toList();
    }
}
