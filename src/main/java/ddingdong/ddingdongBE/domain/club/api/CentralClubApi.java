package ddingdong.ddingdongBE.domain.club.api;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.common.exception.ErrorResponse;
import ddingdong.ddingdongBE.domain.club.controller.dto.request.UpdateClubMemberRequest;
import ddingdong.ddingdongBE.domain.club.controller.dto.request.UpdateClubRequest;
import ddingdong.ddingdongBE.domain.club.controller.dto.response.DetailClubResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Club - Club", description = "Club CentralClub API")
@RequestMapping("/server/club/my")
public interface CentralClubApi {

    @Operation(summary = "동아리원 명단 다운로드 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "명단 다운로드 성공")})
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/club-members/excel")
    ResponseEntity<byte[]> getMyClubMemberListFile(@AuthenticationPrincipal PrincipalDetails principalDetails);

    @Operation(summary = "내 동아리 정보 조회 API")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping
    DetailClubResponse getMyClub(@AuthenticationPrincipal PrincipalDetails principalDetails);

    @Operation(summary = "내 동아리 정보 수정 API")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "AccessToken")
    @PatchMapping
    void updateClub(@AuthenticationPrincipal PrincipalDetails principalDetails,
                    @ModelAttribute UpdateClubRequest param,
                    @RequestPart(name = "profileImage", required = false) List<MultipartFile> profileImage,
                    @RequestPart(name = "introduceImages", required = false) List<MultipartFile> images);

    @Operation(summary = "동아리원 명단 등록 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "명단 등록 성공"),
            @ApiResponse(responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "엑셀 파일이 아님",
                                            value = """
                                                    {
                                                    "status": 400,
                                                     "message": "엑셀 파일이 아닙니다.",
                                                     "timestamp": "2024-08-22T00:08:46.990585"
                                                    }
                                                    """),
                                    @ExampleObject(name = "잘못된 엑셀 파일",
                                            value = """
                                                    {
                                                      "status": 400,
                                                      "message": "올바른 엑셀 파일을 사용해주세요.",
                                                      "timestamp": "2024-08-22T00:08:46.990585"
                                                    }
                                                    """),
                                    @ExampleObject(name = "잘못된 동아리원 역할",
                                            value = """
                                                    {
                                                      "status": 400,
                                                      "message": "동아리원의 역할은 LEADER, EXECUTIVE, MEMBER 중 하나입니다.",
                                                      "timestamp": "2024-08-22T00:08:46.990585"
                                                    }
                                                    """)
                            })
            )
    })
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @PostMapping(value = "/club-members", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    void updateClubMemberList(@AuthenticationPrincipal PrincipalDetails principalDetails,
                              @RequestPart(name = "file") MultipartFile clubMemberListFile);

    @Operation(summary = "동아리원 정보 수정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "수정 성공"),
            @ApiResponse(responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "잘못된 동아리원 역할",
                                            value = """
                                                    {
                                                      "status": 400,
                                                      "message": "동아리원의 역할은 LEADER, EXECUTIVE, MEMBER 중 하나입니다.",
                                                      "timestamp": "2024-08-22T00:08:46.990585"
                                                    }
                                                    """)
                            })
            )
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "AccessToken")
    @PatchMapping("/club-members/{clubMemberId}")
    void updateClubMembers(@PathVariable Long clubMemberId,
                           @RequestBody @Valid UpdateClubMemberRequest request);
}
