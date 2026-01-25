package ddingdong.ddingdongBE.domain.fixzone.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.navercorp.fixturemonkey.FixtureMonkey;
import jakarta.persistence.EntityNotFoundException;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.Location;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import ddingdong.ddingdongBE.domain.fixzone.repository.FixZoneRepository;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.query.AdminFixZoneListQuery;
import ddingdong.ddingdongBE.domain.scorehistory.entity.Score;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FacadeAdminGeneralFixZoneServiceTest extends TestContainerSupport {

    @Autowired
    private FacadeAdminFixZoneService facadeAdminFixZoneService;
    @Autowired
    private FixZoneRepository fixZoneRepository;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private EntityManager entityManager;

    private final FixtureMonkey fixture = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();

    @DisplayName("어드민 - 픽스존 리스트 조회")
    @Test
    void findAll() {
        //given
        Club club = fixture.giveMeBuilder(Club.class)
                .set("id", null)
                .set("user", null)
                .set("clubMembers", null)
                .set("score", Score.from(BigDecimal.ZERO))
                .set("location", Location.from("S1234"))
                .set("deletedAt", null)
                .sample();
        Club savedClub = clubRepository.save(club);
        List<FixZone> fixZones = fixture.giveMeBuilder(FixZone.class)
                .setNull("id")
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
                .set("id", null)
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
                .set("id", null)
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

    @DisplayName("어드민 - 삭제된 Club의 FixZone은 조회되지 않는다")
    @Test
    void findAllExcludesDeletedClubFixZones() {
        //given
        Club club1 = fixture.giveMeBuilder(Club.class)
                .set("id", null)
                .set("user", null)
                .set("clubMembers", null)
                .set("score", Score.from(BigDecimal.ZERO))
                .set("location", Location.from("S1234"))
                .set("deletedAt", null)
                .sample();
        Club savedClub1 = clubRepository.save(club1);

        Club club2 = fixture.giveMeBuilder(Club.class)
                .set("id", null)
                .set("user", null)
                .set("clubMembers", null)
                .set("score", Score.from(BigDecimal.ZERO))
                .set("location", Location.from("S5678"))
                .set("deletedAt", null)
                .sample();
        Club savedClub2 = clubRepository.save(club2);

        FixZone fixZone1 = fixture.giveMeBuilder(FixZone.class)
                .setNull("id")
                .set("club", savedClub1)
                .set("deletedAt", null)
                .sample();
        FixZone fixZone2 = fixture.giveMeBuilder(FixZone.class)
                .setNull("id")
                .set("club", savedClub2)
                .set("deletedAt", null)
                .sample();
        fixZoneRepository.saveAll(List.of(fixZone1, fixZone2));

        // club1 삭제
        clubRepository.delete(savedClub1);

        //when
        List<AdminFixZoneListQuery> result = facadeAdminFixZoneService.getAll();

        //then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).clubName()).isEqualTo(savedClub2.getName());
    }

    @DisplayName("soft delete된 Club을 참조하는 FixZone을 기본 findAll로 조회 시 EntityNotFoundException 발생")
    @Test
    void findAllWithDeletedClubThrowsEntityNotFoundException() {
        //given
        Club club = fixture.giveMeBuilder(Club.class)
                .set("id", null)
                .set("user", null)
                .set("clubMembers", null)
                .set("score", Score.from(BigDecimal.ZERO))
                .set("location", Location.from("S1234"))
                .set("deletedAt", null)
                .sample();
        Club savedClub = clubRepository.save(club);

        FixZone fixZone = fixture.giveMeBuilder(FixZone.class)
                .setNull("id")
                .set("club", savedClub)
                .set("deletedAt", null)
                .sample();
        fixZoneRepository.save(fixZone);

        // Club soft delete
        clubRepository.delete(savedClub);

        // 영속성 컨텍스트 초기화하여 캐시된 엔티티 제거
        entityManager.flush();
        entityManager.clear();

        //when & then
        List<FixZone> fixZones = fixZoneRepository.findAll();
        assertThat(fixZones).hasSize(1);

        // soft delete된 Club에 접근 시 EntityNotFoundException 발생
        assertThatThrownBy(() -> fixZones.get(0).getClub().getName())
                .isInstanceOf(EntityNotFoundException.class);
    }

}
