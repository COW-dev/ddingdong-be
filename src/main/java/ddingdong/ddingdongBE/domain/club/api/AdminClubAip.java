package ddingdong.ddingdongBE.domain.club.api;

import ddingdong.ddingdongBE.domain.club.controller.dto.request.CreateClubRequest;
import ddingdong.ddingdongBE.domain.club.controller.dto.response.AdminClubListResponse;
import ddingdong.ddingdongBE.domain.club.controller.dto.response.MyClubInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Club - Admin", description = "Club Adimin API")
@RequestMapping("/server/admin/clubs")
public interface AdminClubAip {

    @Operation(summary = "동아리 생성 API")
    @ApiResponse(responseCode = "201", description = "동아리 생성 성공")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "AccessToken")
    @PostMapping
    void createClub(@Valid @RequestBody CreateClubRequest request);

    @Operation(summary = "동아리 목록 조회 API")
    @ApiResponse(responseCode = "200", description = "동아리 목록 성공",
            content = @Content(schema = @Schema(implementation = AdminClubListResponse.class)))
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping
    List<AdminClubListResponse> getClubs();

    @Operation(summary = "동아리 삭제 API")
    @ApiResponse(responseCode = "200", description = "동아리 삭제 성공")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @DeleteMapping("/{clubId}")
    void deleteClub(@PathVariable("clubId") Long clubId);

}
