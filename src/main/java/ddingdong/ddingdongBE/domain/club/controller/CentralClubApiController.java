package ddingdong.ddingdongBE.domain.club.controller;

import static ddingdong.ddingdongBE.domain.imageinformation.entity.ImageCategory.*;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.club.controller.dto.request.UpdateClubRequest;
import ddingdong.ddingdongBE.domain.club.controller.dto.response.DetailClubResponse;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.file.service.FileService;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/club/my")
@RequiredArgsConstructor
public class CentralClubApiController {

    private final ClubService clubService;
    private final FileService fileService;

    @GetMapping
    public DetailClubResponse getMyClub(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        return clubService.getMyClub(user.getId());
    }

    @PatchMapping()
    public void updateClub(@AuthenticationPrincipal PrincipalDetails principalDetails,
                           @ModelAttribute UpdateClubRequest param,
                           @RequestPart(name = "uploadFiles", required = false) List<MultipartFile> images) {
        User user = principalDetails.getUser();
        Long updatedClubId = clubService.update(user.getId(), param);

        fileService.deleteImageFile(updatedClubId, CLUB);
        fileService.uploadImageFile(updatedClubId, images, CLUB);
    }

}
