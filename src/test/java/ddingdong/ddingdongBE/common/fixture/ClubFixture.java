package ddingdong.ddingdongBE.common.fixture;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.Location;
import ddingdong.ddingdongBE.domain.club.entity.PhoneNumber;
import ddingdong.ddingdongBE.domain.scorehistory.entity.Score;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.math.BigDecimal;
import java.util.ArrayList;

public class ClubFixture {

    public static Club createClub(final User user) {
        return Club.builder()
                .user(user)
                .clubMembers(new ArrayList<>())
                .name("컴퓨터공학과 동아리")
                .category("학술")
                .tag("프로그래밍, 개발, IT")
                .leader("김동아")
                .phoneNumber(PhoneNumber.from("010-1234-5678"))
                .location(Location.from("S3014"))  // S + 4자리 숫자
                .regularMeeting("매주 수요일 18:00")
                .introduction("컴퓨터공학과 학생들이 함께 공부하고 프로젝트를 진행하는 동아리입니다.")
                .activity("알고리즘 스터디, 웹 개발 프로젝트, 해커톤 참가")
                .ideal("함께 성장하는 개발자 커뮤니티")
                .score(Score.from(BigDecimal.valueOf(85.5)))
                .build();
    }
}
