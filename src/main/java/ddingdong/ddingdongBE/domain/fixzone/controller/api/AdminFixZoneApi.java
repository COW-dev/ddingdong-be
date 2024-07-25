package ddingdong.ddingdongBE.domain.fixzone.controller.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.request.CreateFixZoneCommentRequest;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.response.GetFixZoneResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Fix Zone - Admin", description = "Fix Zone Admin API")
@RequestMapping(value = "/server/admin/fix-zones", produces = APPLICATION_JSON_VALUE)
public interface AdminFixZoneApi {

    @Operation(summary = "Fix Zone 전체 조회 API")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    List<GetFixZoneResponse> getFixZones();

    @Operation(summary = "Fix Zone 요청 처리 완료 API")
    @PatchMapping("/{fix-zone-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "AccessToken")
    void updateFixZoneToComplete(@PathVariable("fix-zone-id") Long fixZoneId);

    @Operation(summary = "Fix Zone 댓글 등록 API")
    @PostMapping("/{fix-zone-id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "AccessToken")
    void createFixZoneComment(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestBody CreateFixZoneCommentRequest request,
        @PathVariable("fix-zone-id") Long fixZoneId
    );

    @Operation(summary = "Fix Zone 댓글 수정 API")
    @PatchMapping("/{fix-zone-id}/comments/{comment-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "AccessToken")
    void updateFixZoneComment(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestBody CreateFixZoneCommentRequest request,
        @PathVariable("fix-zone-id") Long fixZoneId,
        @PathVariable("comment-id") Long commentId
    );

    @Operation(summary = "Fix Zone 댓글 삭제 API")
    @DeleteMapping("/{fix-zone-id}/comments/{comment-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "AccessToken")
    void deleteFixZoneComment(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @PathVariable("fix-zone-id") Long fixZoneId,
        @PathVariable("comment-id") Long commentId
    );

}
