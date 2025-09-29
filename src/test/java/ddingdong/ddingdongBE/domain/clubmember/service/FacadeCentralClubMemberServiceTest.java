package ddingdong.ddingdongBE.domain.clubmember.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.ClubMemberFixture;
import ddingdong.ddingdongBE.common.fixture.UserFixture;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.Position;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.clubmember.entity.ClubMember;
import ddingdong.ddingdongBE.domain.clubmember.repository.ClubMemberRepository;
import ddingdong.ddingdongBE.domain.clubmember.service.dto.command.CreateClubMemberCommand;
import ddingdong.ddingdongBE.domain.clubmember.service.dto.command.UpdateClubMemberCommand;
import ddingdong.ddingdongBE.domain.clubmember.service.dto.command.UpdateClubMemberListCommand;
import ddingdong.ddingdongBE.domain.scorehistory.entity.Score;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
class FacadeCentralClubMemberServiceTest extends TestContainerSupport {

    @Autowired
    private FacadeCentralClubMemberService facadeCentralClubMemberService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private ClubMemberRepository clubMemberRepository;
    @Autowired
    private ClubMemberService clubMemberService;
    @Autowired
    private EntityManager entityManager;

    private final FixtureMonkey fixtureMonkey = FixtureMonkeyFactory.getBuilderIntrospectorMonkey();
    @Autowired
    private ClubService clubService;

    @DisplayName("엑셀 파일을 통해 동아리원 명단을 수정한다.")
    @Test
    void updateAllClubMemberList() throws IOException {
        // given
        User user = UserFixture.createClubUser();
        User savedUser = userRepository.save(user);
        Club club = ClubFixture.createClub(savedUser);
        Club savedClub = clubRepository.save(club);
        
        ClubMember member1 = ClubMember.builder().club(savedClub).name("기존멤버1").build();
        ClubMember member2 = ClubMember.builder().club(savedClub).name("기존멤버2").build();
        ClubMember member3 = ClubMember.builder().club(savedClub).name("기존멤버3").build();
        
        List<ClubMember> existingMembers = clubMemberRepository.saveAll(List.of(member1, member2, member3));
        club.addClubMembers(existingMembers);
        
        // 엑셀 파일 생성 (기존 멤버 중 1, 2번만 유지하고 3번 삭제)
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Members");
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("id");
        header.createCell(1).setCellValue("이름");
        header.createCell(2).setCellValue("학번");
        header.createCell(3).setCellValue("연락처");
        header.createCell(4).setCellValue("비교(임원진) - 영어만");
        header.createCell(5).setCellValue("학과(부)");

        // 기존 멤버 1번만 유지
        Row row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue(existingMembers.get(0).getId());
        row1.createCell(1).setCellValue("수정된멤버1");
        row1.createCell(2).setCellValue("60001234");
        row1.createCell(3).setCellValue("010-1234-5678");
        row1.createCell(4).setCellValue("MEMBER");
        row1.createCell(5).setCellValue("컴퓨터공학과");

        // 기존 멤버 2번만 유지
        Row row2 = sheet.createRow(2);
        row2.createCell(0).setCellValue(existingMembers.get(1).getId());
        row2.createCell(1).setCellValue("수정된멤버2");
        row2.createCell(2).setCellValue("60002345");
        row2.createCell(3).setCellValue("010-2345-6789");
        row2.createCell(4).setCellValue("MEMBER");
        row2.createCell(5).setCellValue("컴퓨터공학과");
        
        workbook.write(out);
        workbook.close();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        MultipartFile validExcelFile = new MockMultipartFile(
                "file",
                "valid_excel.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                in
        );

        UpdateClubMemberListCommand command = UpdateClubMemberListCommand.builder()
                .userId(savedUser.getId())
                .clubMemberListFile(validExcelFile)
                .build();

        // when
        facadeCentralClubMemberService.updateMemberList(command);

        // then
        List<ClubMember> remainingMembers = clubMemberRepository.findAll();
        assertThat(remainingMembers).hasSize(2); // member3는 soft delete됨
        assertThat(remainingMembers)
                .extracting(ClubMember::getName)
                .containsExactlyInAnyOrder("수정된멤버1", "수정된멤버2");
    }

    @DisplayName("동아리원 정보를 수정한다.")
    @Test
    void updateAll() {
        //given
        User savedUser = userRepository.save(fixtureMonkey.giveMeBuilder(User.class).set("id", null).sample());
        Club savedClub = clubRepository.save(fixtureMonkey.giveMeBuilder(Club.class)
                .setNull("id")
                .set("user", savedUser)
                .set("score", Score.from(BigDecimal.ZERO))
                .set("clubMembers", null)
                .sample());
        ClubMember savedClubMember = clubMemberRepository.save(
                fixtureMonkey.giveMeBuilder(ClubMember.class).setNull("id").set("club", savedClub).sample());

        UpdateClubMemberCommand command = UpdateClubMemberCommand.builder()
                .clubMemberId(savedClubMember.getId())
                .name("test")
                .phoneNumber("010-1234-5678")
                .studentNumber("60001234")
                .position(Position.LEADER)
                .department("test").build();

        //when
        facadeCentralClubMemberService.update(command);

        //then
        ClubMember updatedClubMember = clubMemberService.getById(savedClubMember.getId());
        assertThat(updatedClubMember.getName()).isEqualTo("test");
        assertThat(updatedClubMember.getPhoneNumber()).isEqualTo("010-1234-5678");
        assertThat(updatedClubMember.getStudentNumber()).isEqualTo("60001234");
        assertThat(updatedClubMember.getPosition()).isEqualTo(Position.LEADER);
        assertThat(updatedClubMember.getDepartment()).isEqualTo("test");
    }

    @DisplayName("동아리원을 개별 삭제할 수 있다.")
    @Test
    void deleteClubMember() {
        // given
        User user = UserFixture.createClubUser();
        User savedUser = userRepository.save(user);
        Club club = ClubFixture.createClub(savedUser);
        Club savedClub = clubRepository.save(club);
        ClubMember clubMember = ClubMemberFixture.createClubMember(savedClub);
        ClubMember savedClubMember = clubMemberRepository.save(clubMember);
        ClubMember found = clubMemberService.getById(savedClubMember.getId());

        //when
        facadeCentralClubMemberService.delete(savedUser.getId(), savedClubMember.getId());

        //then
        assertThat(found).isNotNull();
        assertThatThrownBy(() -> {
            clubMemberService.getById(savedClubMember.getId());
        }).isInstanceOf(ResourceNotFound.class);
    }

    @DisplayName("자신의 동아리에 속해있지 않은 동아리원을 삭제한다면, 예외를 발생시킨다.")
    @Test
    void deleteClubMemberNotAuth() {
        // given
        User user = UserFixture.createClubUser();
        User savedUser = userRepository.save(user);
        Club club = ClubFixture.createClub(savedUser);
        Club savedClub = clubRepository.save(club);
        ClubMember clubMember = ClubMemberFixture.createClubMember(savedClub);
        ClubMember savedClubMember = clubMemberRepository.save(clubMember);
        clubMemberService.getById(savedClubMember.getId());

        User otherUser = UserFixture.createClubUser();
        User savedOtherUser = userRepository.save(otherUser);
        Club other = ClubFixture.createClub(savedOtherUser);
        Club savedOther = clubRepository.save(other);
        //when & then
        assertThatThrownBy(() -> {
            facadeCentralClubMemberService.delete(savedOther.getId(), savedClubMember.getId());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("동아리원을 개별 생성할 수 있다.")
    @Test
    void create() {
        // given
        User user = UserFixture.createClubUser();
        User savedUser = userRepository.save(user);
        Club club = ClubFixture.createClub(savedUser);
        Club savedClub = clubRepository.save(club);
        CreateClubMemberCommand command = CreateClubMemberCommand.builder()
                .userId(savedUser.getId())
                .name("김철수")
                .studentNumber("60191234")
                .phoneNumber("010-1234-5678")
                .position(Position.MEMBER)
                .department("컴퓨터공학과")
                .build();
        // when
        facadeCentralClubMemberService.create(command);

        // then
        Club found = clubService.getById(savedClub.getId());
        List<ClubMember> clubMembers = found.getClubMembers().stream()
                .filter(clubMember -> Objects.equals(clubMember.getName(), "김철수"))
                .toList();
        assertThat(clubMembers).isNotEmpty();
    }
}
