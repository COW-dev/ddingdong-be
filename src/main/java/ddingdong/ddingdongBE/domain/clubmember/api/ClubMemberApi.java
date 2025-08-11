package ddingdong.ddingdongBE.domain.clubmember.api;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.clubmember.api.dto.request.CreateClubMemberRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "ClubMember - Central", description = "ClubMember API")
@RequestMapping("/server/club-members")
public interface ClubMemberApi {

    @Operation(summary = "동아리원 개별 삭제 API")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponse(responseCode = "204", description = "동아리원 개별 삭제 성공")
    @SecurityRequirement(name = "AccessToken")
    @DeleteMapping("/{id}")
    void delete(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("id") Long clubMemberId);

    @Operation(summary = "동아리원 개별 추가 API")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "201", description = "동아리원 개별 생성 성공")
    @SecurityRequirement(name = "AccessToken")
    @PostMapping
    void create(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody CreateClubMemberRequest request);
}
