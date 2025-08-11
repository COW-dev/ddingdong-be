package ddingdong.ddingdongBE.domain.clubmember.api;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.clubmember.api.dto.request.CreateClubMemberRequest;
import ddingdong.ddingdongBE.domain.clubmember.service.FacadeCentralClubMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClubMemberController implements ClubMemberApi{

    private final FacadeCentralClubMemberService facadeCentralClubMemberService;

    @Override
    public void delete(PrincipalDetails principalDetails, Long clubMemberId) {
        Long userId = principalDetails.getUser().getId();
        facadeCentralClubMemberService.delete(userId, clubMemberId);
    }

    @Override
    public void create(PrincipalDetails principalDetails, CreateClubMemberRequest request) {
        Long userId = principalDetails.getUser().getId();
        facadeCentralClubMemberService.create(request.toCommand(userId));
    }
}
