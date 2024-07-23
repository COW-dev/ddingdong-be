package ddingdong.ddingdongBE.domain.documents.controller;

import ddingdong.ddingdongBE.domain.documents.controller.dto.response.DocumentResponse;
import ddingdong.ddingdongBE.domain.documents.service.DocumentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/server/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping
    public List<DocumentResponse> getAll() {
        return documentService.findAll().stream()
                .map(DocumentResponse::from)
                .toList();
    }
}
