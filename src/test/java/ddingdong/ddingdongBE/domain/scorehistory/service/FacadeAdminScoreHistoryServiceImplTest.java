package ddingdong.ddingdongBE.domain.scorehistory.service;


import static org.assertj.core.api.Assertions.assertThat;

import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.scorehistory.entity.Score;
import ddingdong.ddingdongBE.domain.scorehistory.entity.ScoreCategory;
import ddingdong.ddingdongBE.domain.scorehistory.entity.ScoreHistory;
import ddingdong.ddingdongBE.domain.scorehistory.repository.ScoreHistoryRepository;
import ddingdong.ddingdongBE.domain.scorehistory.service.dto.command.CreateScoreHistoryCommand;
import ddingdong.ddingdongBE.domain.scorehistory.service.dto.query.AdminClubScoreHistoryListQuery;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FacadeAdminScoreHistoryServiceImplTest extends TestContainerSupport {

    @Autowired
    private FacadeAdminScoreHistoryService facadeAdminScoreHistoryService;
    @Autowired
    private ScoreHistoryRepository scoreHistoryRepository;
    @Autowired
    private ClubRepository clubRepository;
    private final FixtureMonkey fixtureMonkey = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();

    @DisplayName("어드민: ScoreHistory 생성")
    @Test
    void findMyScoreHistories() {
        //given
        Club club = fixtureMonkey.giveMeBuilder(Club.class)
                .setNull("user")
                .set("id", null)
                .set("score", Score.from(BigDecimal.ZERO))
                .set("clubMembers", null)
                .set("deletedAt", null)
                .sample();
        Club savedClub = clubRepository.save(club);

        CreateScoreHistoryCommand command = CreateScoreHistoryCommand.builder()
                .clubId(savedClub.getId())
                .scoreCategory(ScoreCategory.BUSINESS_PARTICIPATION)
                .reason("test")
                .amount(BigDecimal.ONE)
                .build();

        // When
        Long createdScoreHistoryId = facadeAdminScoreHistoryService.create(command);

        // Then
        ScoreHistory scoreHistory = scoreHistoryRepository.findById(createdScoreHistoryId).orElseThrow();
        assertThat(scoreHistory).isNotNull();
        assertThat(scoreHistory.getReason()).isEqualTo("test");
        assertThat(scoreHistory.getScoreCategory()).isEqualTo(ScoreCategory.BUSINESS_PARTICIPATION);
    }

    @DisplayName("어드민: ScoreHistory 리스트 조회")
    @Test
    void findAllByClubId() {
        //given
        Club club = fixtureMonkey.giveMeBuilder(Club.class)
                .setNull("user")
                .set("id", null)
                .set("score", Score.from(BigDecimal.ZERO))
                .set("clubMembers", null)
                .set("deletedAt", null)
                .sample();
        Club savedClub = clubRepository.save(club);
        List<ScoreHistory> questions = fixtureMonkey.giveMeBuilder(ScoreHistory.class)
                .set("club", clubRepository.findById(savedClub.getId()).orElseThrow())
                .set("id", null)
                .set("amount", BigDecimal.ONE)
                .sampleList(5);
        scoreHistoryRepository.saveAll(questions);

        // When
        AdminClubScoreHistoryListQuery result = facadeAdminScoreHistoryService.findAllByClubId(savedClub.getId());

        // Then
        assertThat(result.scoreHistories()).hasSize(5);
    }

}
