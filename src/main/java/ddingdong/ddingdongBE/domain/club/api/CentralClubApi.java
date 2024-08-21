package ddingdong.ddingdongBE.domain.club.api;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.club.controller.dto.request.UpdateClubMemberRequest;
import ddingdong.ddingdongBE.domain.club.controller.dto.request.UpdateClubRequest;
import ddingdong.ddingdongBE.domain.club.controller.dto.response.DetailClubResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Club - Club", description = "Club CentralClub API")
@RequestMapping("/server/club/my")
public interface CentralClubApi {

    @SecurityRequirement(name = "AccessToken")
    @GetMapping
    DetailClubResponse getMyClub(@AuthenticationPrincipal PrincipalDetails principalDetails);

    @SecurityRequirement(name = "AccessToken")
    @PatchMapping()
    void updateClub(@AuthenticationPrincipal PrincipalDetails principalDetails,
                    @ModelAttribute UpdateClubRequest param,
                    @RequestPart(name = "profileImage", required = false) List<MultipartFile> profileImage,
                    @RequestPart(name = "introduceImages", required = false) List<MultipartFile> images);

    @SecurityRequirement(name = "AccessToken")
    @PostMapping(value = "/club-members", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    void createClubMembers(@AuthenticationPrincipal PrincipalDetails principalDetails,
                           @RequestPart(name = "file") MultipartFile clubMemberListFile);

    @SecurityRequirement(name = "AccessToken")
    @PutMapping(value = "/club-members")
    void updateClubMembers(@AuthenticationPrincipal PrincipalDetails principalDetails,
                           @RequestPart(value = "data", required = false) UpdateClubMemberRequest request);
}
