package ddingdong.ddingdongBE.domain.club.controller;

import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.*;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.IMAGE;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.club.controller.dto.request.UpdateClubMemberRequest;
import ddingdong.ddingdongBE.domain.club.controller.dto.request.UpdateClubRequest;
import ddingdong.ddingdongBE.domain.club.controller.dto.response.DetailClubResponse;
import ddingdong.ddingdongBE.domain.club.service.ClubMemberService;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.file.service.FileService;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/server/club/my")
@RequiredArgsConstructor
public class CentralClubApiController {

    private final ClubService clubService;
    private final ClubMemberService clubMemberService;
    private final FileService fileService;

    @GetMapping
    public DetailClubResponse getMyClub(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        return clubService.getMyClub(user.getId());
    }

    @PatchMapping()
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

    @PutMapping("/club-members")
    public void updateClubMembers(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                  @RequestBody UpdateClubMemberRequest request) {
        User user = principalDetails.getUser();
        clubMemberService.updateClubMembers(user.getId(), request);
    }

}
