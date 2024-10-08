package ddingdong.ddingdongBE.domain.scorehistory.service;


import static org.assertj.core.api.Assertions.assertThat;

import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.scorehistory.entity.Score;
import ddingdong.ddingdongBE.domain.scorehistory.entity.ScoreHistory;
import ddingdong.ddingdongBE.domain.scorehistory.repository.ScoreHistoryRepository;
import ddingdong.ddingdongBE.domain.scorehistory.service.dto.query.ClubScoreHistoryListQuery;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FacadeAdminScoreHistoryServiceImplTest extends TestContainerSupport {

    @Autowired
    private FacadeClubScoreHistoryService facadeClubScoreHistoryService;
    @Autowired
    private ScoreHistoryRepository scoreHistoryRepository;
    @Autowired
    private ClubRepository clubRepository;
    private final FixtureMonkey fixtureMonkey = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();

    @Test
    @DisplayName("동아리: ScoreHistory 리스트 조회")
    void findMyScoreHistories() {
        //given
        Club club = fixtureMonkey.giveMeBuilder(Club.class)
                .setNull("user")
                .set("score", Score.from(BigDecimal.ZERO))
                .sample();
        Club savedClub = clubRepository.save(club);
        List<ScoreHistory> questions = fixtureMonkey.giveMeBuilder(ScoreHistory.class)
                .set("club", savedClub)
                .set("amount", BigDecimal.ONE)
                .sampleList(5);
        scoreHistoryRepository.saveAll(questions);

        // When
        ClubScoreHistoryListQuery result = facadeClubScoreHistoryService.findMyScoreHistories(
                savedClub.getId());

        // Then
        assertThat(result.scoreHistories()).hasSize(5);
    }

}
