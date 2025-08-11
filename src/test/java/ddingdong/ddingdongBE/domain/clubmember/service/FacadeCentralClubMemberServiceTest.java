package ddingdong.ddingdongBE.domain.clubmember.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.UserFixture;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.Position;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.clubmember.entity.ClubMember;
import ddingdong.ddingdongBE.domain.clubmember.repository.ClubMemberRepository;
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

    @DisplayName("엑셀 파일을 통해 동아리원 명단을 수정한다.")
    @Test
    void updateClubMemberList() throws IOException {
        //given
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

        Row row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue(1);
        row1.createCell(1).setCellValue("5uhwann");
        row1.createCell(2).setCellValue("60001234");
        row1.createCell(3).setCellValue("010-1234-5678");
        row1.createCell(4).setCellValue("LEADER");
        row1.createCell(5).setCellValue("융합소프트웨어학부");

        Row row2 = sheet.createRow(2);
        row2.createCell(0).setCellValue(6);
        row2.createCell(1).setCellValue("5uhwann");
        row2.createCell(2).setCellValue(60001234);
        row2.createCell(3).setCellValue("010-1234-5678");
        row2.createCell(4).setCellValue("LEADER");
        row2.createCell(5).setCellValue("융합소프트웨어학부");
        workbook.write(out);
        workbook.close();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        MultipartFile validExcelFile = new MockMultipartFile(
                "file",
                "valid_excel.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                in
        );

        User savedUser = userRepository.save(fixtureMonkey.giveMeOne(User.class));
        Club savedClub = clubRepository.save(fixtureMonkey.giveMeBuilder(Club.class)
                .set("user", savedUser)
                .set("score", Score.from(BigDecimal.ZERO))
                .set("clubMembers", List.of())
                .set("deletedAt", null)
                .sample());
        List<ClubMember> clubMembers = fixtureMonkey.giveMeBuilder(ClubMember.class)
                .set("club", savedClub)
                .set("deletedAt", null)
                .sampleList(5);
        clubMemberRepository.saveAll(clubMembers);
        entityManager.flush();
        entityManager.clear();

        UpdateClubMemberListCommand command = UpdateClubMemberListCommand.builder()
                .userId(savedUser.getId())
                .clubMemberListFile(validExcelFile)
                .build();

        //when
        facadeCentralClubMemberService.updateMemberList(command);

        //then
        List<ClubMember> updatedClubMemberList = clubMemberRepository.findAll();
        assertThat(updatedClubMemberList.size()).isEqualTo(2);
    }

    @DisplayName("동아리원 정보를 수정한다.")
    @Test
    void update() {
        //given
        User savedUser = userRepository.save(fixtureMonkey.giveMeOne(User.class));
        Club savedClub = clubRepository.save(fixtureMonkey.giveMeBuilder(Club.class)
                .set("user", savedUser)
                .set("score", Score.from(BigDecimal.ZERO))
                .set("clubMembers", null)
                .sample());
        ClubMember savedClubMember = clubMemberRepository.save(
                fixtureMonkey.giveMeBuilder(ClubMember.class).set("club", savedClub).sample());

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
}
