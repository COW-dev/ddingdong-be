package ddingdong.ddingdongBE.domain.clubmember.api;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.clubmember.service.FacadeCentralClubMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClubMemberController implements ClubMemberApi{

    private final FacadeCentralClubMemberService facadeCentralClubMemberService;

    @Override
    public void delete(final PrincipalDetails principalDetails, final Long clubMemberId) {
        Long userId = principalDetails.getUser().getId();
        facadeCentralClubMemberService.delete(userId, clubMemberId);
    }
}
