package ddingdong.ddingdongBE.domain.documents.api;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.documents.controller.dto.request.CreateDocumentRequest;
import ddingdong.ddingdongBE.domain.documents.controller.dto.request.UpdateDocumentRequest;
import ddingdong.ddingdongBE.domain.documents.controller.dto.response.AdminDocumentListResponse;
import ddingdong.ddingdongBE.domain.documents.controller.dto.response.AdminDocumentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Document - Admin", description = "Document Admin API")
@RequestMapping("/server/admin/documents")
public interface AdminDocumentApi {

    @Operation(summary = "어드민 자료실 업로드 API")
    @ApiResponse(responseCode = "201", description = "어드민 자료실 업로드 성공")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "AccessToken")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    void createDocument(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @ModelAttribute CreateDocumentRequest createDocumentRequest,
        @RequestPart(name = "uploadFiles") List<MultipartFile> uploadFiles
    );

    @Operation(summary = "어드민 자료실 전체 조회 API")
    @ApiResponse(responseCode = "200", description = "어드민 자료실 전체 조회 성공",
        content = @Content(schema = @Schema(implementation = AdminDocumentListResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping
    List<AdminDocumentListResponse> getAdminDocuments();

    @Operation(summary = "어드민 자료실 상세 조회 API")
    @ApiResponse(responseCode = "200", description = "어드민 자료실 상세 조회 성공",
        content = @Content(schema = @Schema(implementation = AdminDocumentResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/{documentId}")
    AdminDocumentResponse getAdminDocument(@PathVariable Long documentId);

    @Operation(summary = "어드민 자료실 수정 API")
    @ApiResponse(responseCode = "200", description = "어드민 자료실 수정 성공")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @PatchMapping(value = "/{documentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    void updateDocument(@PathVariable Long documentId,
        @ModelAttribute UpdateDocumentRequest updateDocumentRequest,
        @RequestPart(name = "uploadFiles", required = false) List<MultipartFile> uploadFiles);

    @Operation(summary = "어드민 자료실 삭제 API")
    @ApiResponse(responseCode = "200", description = "어드민 자료실 삭제 성공")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @DeleteMapping("/{documentId}")
    void deleteDocument(@PathVariable Long documentId);
}
