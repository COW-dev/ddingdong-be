package ddingdong.ddingdongBE.domain.club.service;

import static ddingdong.ddingdongBE.domain.club.entity.RecruitmentStatus.END_RECRUIT;
import static ddingdong.ddingdongBE.domain.club.entity.RecruitmentStatus.RECRUITING;
import static org.assertj.core.api.Assertions.assertThat;

import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.Location;
import ddingdong.ddingdongBE.domain.club.entity.PhoneNumber;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.club.service.dto.query.UserClubListQuery;
import ddingdong.ddingdongBE.domain.club.service.dto.query.UserClubQuery;
import ddingdong.ddingdongBE.domain.scorehistory.entity.Score;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FacadeUserClubServiceImplTest extends TestContainerSupport {

    @Autowired
    private FacadeUserClubService facadeUserClubService;
    @Autowired
    private ClubRepository clubRepository;

    private final FixtureMonkey fixture = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();

    @DisplayName("유저: 동아리 목록 조회 - 모집 가능")
    @Test
    void findAllWithRECRUITING() {
        //given
        LocalDateTime queriedAt = LocalDateTime.of(2024, 9, 15, 0, 0);
        List<Club> clubs = fixture.giveMeBuilder(Club.class)
                .set("user", null)
                .set("score", Score.from(BigDecimal.ZERO))
                .set("phoneNumber", PhoneNumber.from("010-1234-5678"))
                .set("location", Location.from("S1111"))
                .set("startRecruitPeriod", LocalDateTime.of(2024, 9, 1, 0, 0))
                .set("endRecruitPeriod", LocalDateTime.of(2024, 12, 31, 0, 0))
                .set("clubMembers", null)
                .set("deletedAt", null)
                .sampleList(5);
        clubRepository.saveAll(clubs);

        //when
        List<UserClubListQuery> result =
                facadeUserClubService.findAllWithRecruitTimeCheckPoint(queriedAt);

        //then
        assertThat(result).hasSize(5);
        assertThat(result.stream()
                .allMatch((query -> query.recruitStatus().equals(RECRUITING.getText())))).isTrue();
    }

    @DisplayName("유저: 동아리 목록 조회 - 모집 마감")
    @Test
    void findAllWithEND_RECRUIT() {
        //given
        LocalDateTime queriedAt = LocalDateTime.of(2025, 9, 15, 0, 0);
        List<Club> clubs = fixture.giveMeBuilder(Club.class)
                .set("user", null)
                .set("score", Score.from(BigDecimal.ZERO))
                .set("phoneNumber", PhoneNumber.from("010-1234-5678"))
                .set("location", Location.from("S1111"))
                .set("startRecruitPeriod", LocalDateTime.of(2024, 9, 1, 0, 0))
                .set("endRecruitPeriod", LocalDateTime.of(2024, 12, 31, 0, 0))
                .set("clubMembers", null)
                .set("deletedAt", null)
                .sampleList(5);
        clubRepository.saveAll(clubs);

        //when
        List<UserClubListQuery> result =
                facadeUserClubService.findAllWithRecruitTimeCheckPoint(queriedAt);

        //then
        assertThat(result).hasSize(5);
        assertThat(result.stream()
                .allMatch((query -> query.recruitStatus().equals(END_RECRUIT.getText())))).isTrue();
    }

    @DisplayName("유저: 동아리 정보 상세 조회")
    @Test
    void getClub() {
        //given
        Club savedClub = clubRepository.save(fixture.giveMeBuilder(Club.class)
                .set("user", null)
                .set("score", Score.from(BigDecimal.ZERO))
                .set("phoneNumber", PhoneNumber.from("010-1234-5678"))
                .set("location", Location.from("S1111"))
                .set("startRecruitPeriod", LocalDateTime.of(2024, 9, 1, 0, 0))
                .set("startRecruitPeriod", LocalDateTime.of(2024, 12, 31, 0, 0))
                .set("clubMembers", null)
                .set("deletedAt", null)
                .sample());

        //when
        UserClubQuery result = facadeUserClubService.getClub(savedClub.getId());

        //then
        assertThat(result).isNotNull();
    }


}
