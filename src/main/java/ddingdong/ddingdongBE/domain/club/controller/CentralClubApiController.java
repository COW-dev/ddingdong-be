package ddingdong.ddingdongBE.domain.club.controller;

import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.CLUB_INTRODUCE;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.CLUB_PROFILE;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.IMAGE;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.club.api.CentralClubApi;
import ddingdong.ddingdongBE.domain.club.controller.dto.request.UpdateClubMemberRequest;
import ddingdong.ddingdongBE.domain.club.controller.dto.request.UpdateClubRequest;
import ddingdong.ddingdongBE.domain.club.controller.dto.response.DetailClubResponse;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.clubmember.service.FacadeClubMemberService;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.file.service.FileService;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CentralClubApiController implements CentralClubApi {

    private final ClubService clubService;
    private final FacadeClubMemberService facadeClubMemberService;
    private final FileService fileService;

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

    public DetailClubResponse getMyClub(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        return clubService.getMyClub(user.getId());
    }

    public void updateClub(@AuthenticationPrincipal PrincipalDetails principalDetails,
                           @ModelAttribute UpdateClubRequest param,
                           @RequestPart(name = "profileImage", required = false) List<MultipartFile> profileImage,
                           @RequestPart(name = "introduceImages", required = false) List<MultipartFile> images) {
        User user = principalDetails.getUser();
        Long updatedClubId = clubService.update(user.getId(), param);

        if (profileImage != null) {
            fileService.deleteFile(updatedClubId, IMAGE, CLUB_PROFILE);
            fileService.uploadFile(updatedClubId, profileImage, IMAGE, CLUB_PROFILE);
        }

        if (images != null) {
            fileService.deleteFile(updatedClubId, IMAGE, CLUB_INTRODUCE);
            fileService.uploadFile(updatedClubId, images, IMAGE, CLUB_INTRODUCE);
        }
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
