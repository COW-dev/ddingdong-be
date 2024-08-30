package ddingdong.ddingdongBE.domain.club.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.club.api.CentralClubApi;
import ddingdong.ddingdongBE.domain.club.controller.dto.request.UpdateClubMemberRequest;
import ddingdong.ddingdongBE.domain.club.controller.dto.request.UpdateClubRequest;
import ddingdong.ddingdongBE.domain.club.controller.dto.response.ClubResponse;
import ddingdong.ddingdongBE.domain.club.service.FacadeClubMemberService;
import ddingdong.ddingdongBE.domain.club.service.FacadeClubService;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CentralClubApiController implements CentralClubApi {

    private final FacadeClubService facadeClubService;
    private final FacadeClubMemberService facadeClubMemberService;

    @Override
    public ResponseEntity<byte[]> getMyClubMemberListFile(PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        byte[] clubMemberListFileData = facadeClubMemberService.getClubMemberListFile(user.getId());
        String filename = "동아리원명단.xlsx";
        String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFilename);

        return ResponseEntity.ok()
                .headers(headers)
                .body(clubMemberListFileData);
    }

    public ClubResponse getMyClub(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        return facadeClubService.getMyClub(user.getId());
    }

    @Override
    public void updateClub(PrincipalDetails principalDetails, UpdateClubRequest request) {
        User user = principalDetails.getUser();
        facadeClubService.updateClub(user.getId(), request.toCommand());
    }


    @Override
    public void updateClubMemberList(PrincipalDetails principalDetails, MultipartFile clubMemberListFile) {
        User user = principalDetails.getUser();
        facadeClubMemberService.updateMemberList(user.getId(), clubMemberListFile);
    }

    @Override
    public void updateClubMembers(Long clubMemberId,
                                  UpdateClubMemberRequest request) {
        facadeClubMemberService.update(clubMemberId, request.toCommand());
    }
}
