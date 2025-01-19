package ddingdong.ddingdongBE.domain.fixzone.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZoneComment;
import ddingdong.ddingdongBE.domain.fixzone.repository.FixZoneCommentRepository;
import ddingdong.ddingdongBE.domain.fixzone.repository.FixZoneRepository;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.command.CreateFixZoneCommentCommand;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.command.UpdateFixZoneCommentCommand;
import ddingdong.ddingdongBE.domain.scorehistory.entity.Score;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FacadeAdminFixZoneCommentServiceImplTest extends TestContainerSupport {

    @Autowired
    private FacadeAdminFixZoneCommentService facadeAdminFixZoneCommentService;
    @Autowired
    private FixZoneRepository fixZoneRepository;
    @Autowired
    private FixZoneCommentRepository fixZoneCommentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClubRepository clubRepository;

    private final FixtureMonkey fixture = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();

    private User savedUser;
    private Club savedClub;
    private FixZone savedFixZone;

    @BeforeEach
    void setUp() {
        savedUser = userRepository.save(fixture.giveMeOne(User.class));
        savedClub = clubRepository.save(fixture.giveMeBuilder(Club.class)
                .set("user", savedUser)
                .set("clubMembers", null)
                .set("score", Score.from(BigDecimal.ZERO))
                .set("deletedAt", null)
                .sample());
        savedFixZone = fixZoneRepository.save(fixture.giveMeBuilder(FixZone.class)
                .set("club", savedClub)
                .set("deletedAt", null)
                .sample());
    }

    @DisplayName("어드민 - 픽스존 댓글 생성")
    @Test
    void create() {
        //given
        CreateFixZoneCommentCommand command =
                new CreateFixZoneCommentCommand(savedUser.getId(), savedFixZone.getId(), "test");
        //when
        Long createdFixZoneCommentId = facadeAdminFixZoneCommentService.create(command);

        //then
        Optional<FixZoneComment> result = fixZoneCommentRepository.findById(createdFixZoneCommentId);
        assertThat(result.isPresent()).isTrue();
    }

    @DisplayName("어드민 - 픽스존 댓글 수정")
    @Test
    void update() {
        //given
        FixZoneComment fixZoneComment = fixture.giveMeBuilder(FixZoneComment.class)
                .set("fixZone", savedFixZone)
                .set("club", savedClub)
                .set("deletedAt", null)
                .sample();
        FixZoneComment savedFixZoneComment = fixZoneCommentRepository.save(fixZoneComment);

        UpdateFixZoneCommentCommand command = new UpdateFixZoneCommentCommand(savedFixZoneComment.getId(), "test");

        //when
        Long updatedFixZoneCommentId = facadeAdminFixZoneCommentService.update(command);

        //then
        FixZoneComment result = fixZoneCommentRepository.findById(updatedFixZoneCommentId).orElseThrow();
        assertThat(result.getContent()).isEqualTo("test");
    }

    @DisplayName("어드민 - 픽스존 댓글 삭제")
    @Test
    void delete() {
        //given
        FixZoneComment fixZoneComment = fixture.giveMeBuilder(FixZoneComment.class)
                .set("fixZone", savedFixZone)
                .set("club", savedClub)
                .set("deletedAt", null)
                .sample();
        FixZoneComment savedFixZoneComment = fixZoneCommentRepository.save(fixZoneComment);

        //when
        facadeAdminFixZoneCommentService.delete(savedFixZoneComment.getId());

        //then
        Optional<FixZoneComment> result = fixZoneCommentRepository.findById(savedFixZoneComment.getId());
        assertThat(result.isPresent()).isFalse();
    }


}
