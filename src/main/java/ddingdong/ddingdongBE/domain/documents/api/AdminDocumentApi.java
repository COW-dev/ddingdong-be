package ddingdong.ddingdongBE.domain.documents.api;


import ddingdong.ddingdongBE.domain.documents.controller.dto.request.GenerateDocumentRequest;
import ddingdong.ddingdongBE.domain.documents.controller.dto.request.ModifyDocumentRequest;
import ddingdong.ddingdongBE.domain.documents.controller.dto.response.AdminDetailDocumentResponse;
import ddingdong.ddingdongBE.domain.documents.controller.dto.response.AdminDocumentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "AccessToken")
    void generate(@ModelAttribute GenerateDocumentRequest generateDocumentRequest,
                  @RequestPart(name = "uploadFiles") List<MultipartFile> uploadFiles);

    @Operation(summary = "어드민 자료실 목록 조회 API")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    List<AdminDocumentResponse> getAll();

    @Operation(summary = "어드민 자료실 상세 조회 API")
    @GetMapping("/{documentId}")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    AdminDetailDocumentResponse getDetail(@PathVariable Long documentId);

    @Operation(summary = "어드민 자료실 수정 API")
    @PatchMapping(value = "/{documentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    void modifyDocument(@PathVariable Long documentId,
                        @ModelAttribute ModifyDocumentRequest modifyDocumentRequest,
                        @RequestPart(name = "uploadFiles", required = false) List<MultipartFile> uploadFiles);

    @Operation(summary = "어드민 자료실 삭제 API")
    @DeleteMapping("/{documentId}")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    void deleteDocument(@PathVariable Long documentId);


}
