package ddingdong.ddingdongBE.common.fixture;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;

public class FixZoneFixture {

    public static FixZone createFixZone(Club club) {
        return FixZone.builder()
                .club(club)
                .title("픽스존 제목")
                .content("픽스존 내용")
                .isCompleted(false)
                .build();
    }

    public static FixZone createCompletedFixZone(Club club) {
        return FixZone.builder()
                .club(club)
                .title("완료된 픽스존")
                .content("완료된 픽스존 내용")
                .isCompleted(true)
                .build();
    }
}