package ddingdong.ddingdongBE.domain.fixzone.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import ddingdong.ddingdongBE.domain.fixzone.repository.FixZoneRepository;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.query.AdminFixZoneListQuery;
import ddingdong.ddingdongBE.domain.scorehistory.entity.Score;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FacadeAdminFixZoneServiceImplTest extends TestContainerSupport {

    @Autowired
    private FacadeAdminFixZoneService facadeAdminFixZoneService;
    @Autowired
    private FixZoneRepository fixZoneRepository;
    @Autowired
    private ClubRepository clubRepository;

    private final FixtureMonkey fixture = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();

    @DisplayName("어드민 - 픽스존 리스트 조회")
    @Test
    void findAl() {
        //given
        Club club = fixture.giveMeBuilder(Club.class)
                .set("user", null)
                .set("clubMembers", null)
                .set("score", Score.from(BigDecimal.ZERO))
                .sample();
        Club savedClub = clubRepository.save(club);
        List<FixZone> fixZones = fixture.giveMeBuilder(FixZone.class)
                .set("club", savedClub)
                .set("deletedAt", null)
                .sampleList(5);
        fixZoneRepository.saveAll(fixZones);

        //when
        List<AdminFixZoneListQuery> result = facadeAdminFixZoneService.getAll();

        //then
        assertThat(result.size()).isEqualTo(5);
    }

    @DisplayName("어드민 - 픽스존 완료 여부 수정")
    @Test
    void updateToComplete() {
        //given
        FixZone fixZone = fixture.giveMeBuilder(FixZone.class)
                .set("club", null)
                .set("isCompleted", false)
                .set("deletedAt", null)
                .sample();
        FixZone savedFixZone = fixZoneRepository.save(fixZone);

        //when
        facadeAdminFixZoneService.updateToComplete(savedFixZone.getId());

        //then
        FixZone result = fixZoneRepository.findById(savedFixZone.getId()).orElseThrow();
        assertThat(result.isCompleted()).isTrue();
    }

    @DisplayName("어드민 - 픽스존 삭제")
    @Test
    void delete() {
        //given
        FixZone fixZone = fixture.giveMeBuilder(FixZone.class)
                .set("club", null)
                .set("isCompleted", false)
                .set("deletedAt", null)
                .sample();
        FixZone savedFixZone = fixZoneRepository.save(fixZone);

        //when
        facadeAdminFixZoneService.delete(savedFixZone.getId());

        //then
        Optional<FixZone> result = fixZoneRepository.findById(savedFixZone.getId());
        assertThat(result.isPresent()).isFalse();
    }



}
