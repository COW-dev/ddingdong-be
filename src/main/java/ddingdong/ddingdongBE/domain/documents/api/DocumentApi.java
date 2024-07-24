package ddingdong.ddingdongBE.domain.documents.api;


import ddingdong.ddingdongBE.domain.documents.controller.dto.response.DetailDocumentResponse;
import ddingdong.ddingdongBE.domain.documents.controller.dto.response.DocumentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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
    @SecurityRequirement(name = "AccessToken")
    List<DocumentResponse> getAll();

    @Operation(summary = "자료실 상세 조회 API")
    @GetMapping("/{documentId}")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    DetailDocumentResponse getDetail(@PathVariable Long documentId);

}
