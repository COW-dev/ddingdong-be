package ddingdong.ddingdongBE.domain.clubmember.service.dto.query;

import ddingdong.ddingdongBE.domain.clubmember.entity.ClubMember;
import java.util.List;

public record AllClubMemberInfoQuery(
    String clubName,
    List<ClubMember> clubMembers
) {

    public static AllClubMemberInfoQuery of(String clubName, List<ClubMember> clubMembers) {
        return new AllClubMemberInfoQuery(clubName, clubMembers);
    }
}
