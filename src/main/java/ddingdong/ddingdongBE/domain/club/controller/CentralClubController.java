package ddingdong.ddingdongBE.domain.club.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.club.api.CentralClubApi;
import ddingdong.ddingdongBE.domain.club.controller.dto.request.UpdateClubInfoRequest;
import ddingdong.ddingdongBE.domain.club.controller.dto.request.UpdateClubMemberRequest;
import ddingdong.ddingdongBE.domain.club.controller.dto.response.AllClubMemberInfoResponse;
import ddingdong.ddingdongBE.domain.club.controller.dto.response.MyClubInfoResponse;
import ddingdong.ddingdongBE.domain.club.service.FacadeCentralClubService;
import ddingdong.ddingdongBE.domain.club.service.dto.query.MyClubInfoQuery;
import ddingdong.ddingdongBE.domain.clubmember.service.FacadeCentralClubMemberService;
import ddingdong.ddingdongBE.domain.clubmember.service.dto.command.UpdateClubMemberListCommand;
import ddingdong.ddingdongBE.domain.clubmember.service.dto.query.AllClubMemberInfoQuery;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
public class CentralClubController implements CentralClubApi {

    private final FacadeCentralClubService facadeCentralClubService;
    private final FacadeCentralClubMemberService facadeCentralClubMemberService;

    @Override
    public ResponseEntity<byte[]> getMyClubMemberListFile(PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        byte[] clubMemberListFileData = facadeCentralClubMemberService.getClubMemberListFile(user.getId());
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
    public AllClubMemberInfoResponse getMyClubMembers(PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        AllClubMemberInfoQuery query = facadeCentralClubMemberService.getAllMyClubMember(user.getId());
        return AllClubMemberInfoResponse.from(query);
    }

    @Override
    public void updateClub(PrincipalDetails principalDetails, UpdateClubInfoRequest request) {
        User user = principalDetails.getUser();
        facadeCentralClubService.updateClubInfo(request.toCommand(user.getId()));
    }

    @Override
    public void updateClubMemberList(PrincipalDetails principalDetails, MultipartFile clubMemberListFile) {
        User user = principalDetails.getUser();
        UpdateClubMemberListCommand command = UpdateClubMemberListCommand.builder()
                .userId(user.getId())
                .clubMemberListFile(clubMemberListFile)
                .build();
        facadeCentralClubMemberService.updateMemberList(command);
    }

    @Override
    public void updateClubMembers(Long clubMemberId, UpdateClubMemberRequest request) {
        facadeCentralClubMemberService.update(request.toCommand(clubMemberId));
    }
}
