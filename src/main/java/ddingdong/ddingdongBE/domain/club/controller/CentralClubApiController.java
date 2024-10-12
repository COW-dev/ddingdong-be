package ddingdong.ddingdongBE.domain.club.controller;

import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.CLUB_INTRODUCE;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.CLUB_PROFILE;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.IMAGE;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.club.api.CentralClubApi;
import ddingdong.ddingdongBE.domain.club.controller.dto.request.UpdateClubInfoRequest;
import ddingdong.ddingdongBE.domain.club.controller.dto.request.UpdateClubMemberRequest;
import ddingdong.ddingdongBE.domain.club.controller.dto.response.MyClubInfoResponse;
import ddingdong.ddingdongBE.domain.club.service.FacadeCentralClubService;
import ddingdong.ddingdongBE.domain.club.service.dto.query.MyClubInfoQuery;
import ddingdong.ddingdongBE.domain.clubmember.service.FacadeClubMemberService;
import ddingdong.ddingdongBE.domain.clubmember.service.dto.command.UpdateClubMemberListCommand;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CentralClubApiController implements CentralClubApi {

    private final FacadeCentralClubService facadeCentralClubService;
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

    @Override
    public MyClubInfoResponse getMyClub(PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        MyClubInfoQuery query = facadeCentralClubService.getMyClubInfo(user.getId());
        return MyClubInfoResponse.from(query);
    }

    @Override
    public void updateClub(PrincipalDetails principalDetails,
                           UpdateClubInfoRequest request,
                           List<MultipartFile> profileImage,
                           List<MultipartFile> images) {
        User user = principalDetails.getUser();
        Long updatedClubId = facadeCentralClubService.updateClubInfo(request.toCommand(user.getId()));

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
        UpdateClubMemberListCommand command = UpdateClubMemberListCommand.builder()
                .userId(user.getId())
                .clubMemberListFile(clubMemberListFile)
                .build();
        facadeClubMemberService.updateMemberList(command);
    }

    @Override
    public void updateClubMembers(Long clubMemberId, UpdateClubMemberRequest request) {
        facadeClubMemberService.update(request.toCommand(clubMemberId));
    }
}
