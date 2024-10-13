package ddingdong.ddingdongBE.domain.club.api;

import ddingdong.ddingdongBE.domain.club.controller.dto.response.UserClubListResponse;
import ddingdong.ddingdongBE.domain.club.controller.dto.response.UserClubResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

@Tag(name = "Club - User", description = "Club User API")
@RequestMapping("/server/clubs")
public interface UserClubApi {

    @Operation(summary = "동아리 목록 조회 API")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "200", description = "동아리 목록 조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserClubListResponse.class))))
    @GetMapping
    List<UserClubListResponse> getClubs();

    @Operation(summary = "동아리 정보 조회 API")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "200", description = "동아리 정보 조회 성공",
            content = @Content(schema = @Schema(implementation = UserClubResponse.class)))
    @GetMapping("/{clubId}")
    UserClubResponse getDetailClub(@PathVariable("clubId") Long clubId);

}
