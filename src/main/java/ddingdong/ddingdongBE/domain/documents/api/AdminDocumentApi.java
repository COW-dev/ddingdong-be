package ddingdong.ddingdongBE.domain.documents.api;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.documents.controller.dto.request.CreateDocumentRequest;
import ddingdong.ddingdongBE.domain.documents.controller.dto.request.UpdateDocumentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Document - Admin", description = "Document Admin API")
@RequestMapping("/server/admin/documents")
public interface AdminDocumentApi {

    @Operation(summary = "어드민 자료실 업로드 API")
    @ApiResponse(responseCode = "201", description = "어드민 자료실 업로드 성공")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "AccessToken")
    @PostMapping
    void createDocument(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @Valid @RequestBody CreateDocumentRequest createDocumentRequest
    );

    @Operation(summary = "어드민 자료실 수정 API")
    @ApiResponse(responseCode = "200", description = "어드민 자료실 수정 성공")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @PatchMapping(value = "/{documentId}")
    void updateDocument(
        @PathVariable Long documentId,
        @RequestBody UpdateDocumentRequest updateDocumentRequest
    );

    @Operation(summary = "어드민 자료실 삭제 API")
    @ApiResponse(responseCode = "200", description = "어드민 자료실 삭제 성공")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @DeleteMapping("/{documentId}")
    void deleteDocument(@PathVariable(name = "documentId") Long documentId);
}
