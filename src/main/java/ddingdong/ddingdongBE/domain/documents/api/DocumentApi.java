package ddingdong.ddingdongBE.domain.documents.api;


import ddingdong.ddingdongBE.domain.documents.controller.dto.request.GetDocumentPagingRequest;
import ddingdong.ddingdongBE.domain.documents.controller.dto.response.DocumentListResponse;
import ddingdong.ddingdongBE.domain.documents.controller.dto.response.DocumentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "자료실 전체 조회 API")
    @ApiResponse(responseCode = "200", description = "자료실 전체 조회 성공",
        content = @Content(schema = @Schema(implementation = DocumentListResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    List<DocumentListResponse> getDocumentList(GetDocumentPagingRequest request);

    @Operation(summary = "자료실 상세 조회 API")
    @ApiResponse(responseCode = "200", description = "자료실 상세 조회 성공",
        content = @Content(schema = @Schema(implementation = DocumentResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{documentId}")
    DocumentResponse getDocument(@PathVariable Long documentId);

}
