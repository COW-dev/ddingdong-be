package ddingdong.ddingdongBE.common.fixture;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.clubmember.entity.ClubMember;

public class ClubMemberFixture {

    public static ClubMember createClubMember(Club club) {
        return ClubMember.builder()
                .name("웨이드")
                .club(club)
                .build();
    }
}
