package ddingdong.ddingdongBE.domain.documents.api;


import ddingdong.ddingdongBE.domain.documents.controller.dto.request.GetDocumentByPageRequest;
import ddingdong.ddingdongBE.domain.documents.controller.dto.response.DetailDocumentResponse;
import ddingdong.ddingdongBE.domain.documents.controller.dto.response.GetDocumentByPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Document", description = "Document API")
@RequestMapping("/server/documents")
public interface DocumentApi {

    @Operation(summary = "자료실 목록 조회 API")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    GetDocumentByPageResponse getAllDocuments(GetDocumentByPageRequest request);

    @Operation(summary = "자료실 상세 조회 API")
    @GetMapping("/{documentId}")
    @ResponseStatus(HttpStatus.OK)
    DetailDocumentResponse getDetailDocument(@PathVariable Long documentId);

}
