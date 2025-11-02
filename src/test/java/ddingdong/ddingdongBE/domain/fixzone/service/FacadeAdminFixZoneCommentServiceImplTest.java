package ddingdong.ddingdongBE.domain.fixzone.service;

import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.FixZoneCommentFixture;
import ddingdong.ddingdongBE.common.fixture.FixZoneFixture;
import ddingdong.ddingdongBE.common.fixture.UserFixture;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZoneComment;
import ddingdong.ddingdongBE.domain.fixzone.repository.FixZoneCommentRepository;
import ddingdong.ddingdongBE.domain.fixzone.repository.FixZoneRepository;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.command.CreateFixZoneCommentCommand;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.command.UpdateFixZoneCommentCommand;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
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

    private User savedAdmin;
    private FixZone savedFixZone;

    @BeforeEach
    void setUp() {
        savedAdmin = userRepository.save(UserFixture.createAdminUser());
        User savedClubUser = userRepository.save(UserFixture.createClubUser());
        Club savedClub = clubRepository.save(ClubFixture.createClub(savedClubUser));
        savedFixZone = fixZoneRepository.save(FixZoneFixture.createFixZone(savedClub));
    }

    @DisplayName("어드민 - 픽스존 댓글 생성")
    @Test
    void create() {
        //given
        CreateFixZoneCommentCommand command = new CreateFixZoneCommentCommand(
                savedAdmin,
                savedFixZone.getId(),
                "새로운 댓글 내용"
        );

        //when
        Long commentId = facadeAdminFixZoneCommentService.create(command);

        //then
        FixZoneComment result = fixZoneCommentRepository.findById(commentId).orElseThrow();
        assertThat(result.getContent()).isEqualTo("새로운 댓글 내용");
        assertThat(result.getUser()).isEqualTo(savedAdmin);
        assertThat(result.getFixZone()).isEqualTo(savedFixZone);
    }

    @DisplayName("어드민 - 픽스존 댓글 수정")
    @Test
    void update() {
        //given
        FixZoneComment fixZoneComment = FixZoneCommentFixture.createFixZoneComment(savedAdmin, savedFixZone);
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
        FixZoneComment fixZoneComment = FixZoneCommentFixture.createFixZoneComment(savedAdmin, savedFixZone);
        FixZoneComment savedFixZoneComment = fixZoneCommentRepository.save(fixZoneComment);

        //when
        facadeAdminFixZoneCommentService.delete(savedFixZoneComment.getId());

        //then
        Optional<FixZoneComment> result = fixZoneCommentRepository.findById(savedFixZoneComment.getId());
        assertThat(result.isPresent()).isFalse();
    }


}
