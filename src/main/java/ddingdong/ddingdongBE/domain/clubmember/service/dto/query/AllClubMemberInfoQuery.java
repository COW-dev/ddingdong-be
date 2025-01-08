package ddingdong.ddingdongBE.domain.clubmember.service.dto.query;

import java.util.List;

public record AllClubMemberInfoQuery(
    String clubName,
    List<ClubMemberListQuery> clubMemberListQueries
) {

    public static AllClubMemberInfoQuery of(String clubName, List<ClubMemberListQuery> clubMemberListQueries) {
        return new AllClubMemberInfoQuery(clubName, clubMemberListQueries);
    }
}
