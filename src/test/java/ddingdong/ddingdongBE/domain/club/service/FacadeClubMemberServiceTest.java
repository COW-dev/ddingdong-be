package ddingdong.ddingdongBE.domain.club.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.ClubMember;
import ddingdong.ddingdongBE.domain.club.entity.Position;
import ddingdong.ddingdongBE.domain.club.repository.ClubMemberRepository;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.club.service.dto.UpdateClubMemberCommand;
import ddingdong.ddingdongBE.domain.scorehistory.entity.Score;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FacadeClubMemberServiceTest extends TestContainerSupport {

    @Autowired
    private FacadeClubMemberService facadeClubMemberService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private ClubMemberRepository clubMemberRepository;
    @Autowired
    private ClubMemberService clubMemberService;

    private final FixtureMonkey fixtureMonkey = FixtureMonkeyFactory.getBuilderIntrospectorNotNullMonkey();

//    @DisplayName("엑셀 파일을 통해 동아리원 명단을 수정한다.")
//    @Test
//    void updateClubList() throws IOException {
//        //given
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("Members");
//        Row header = sheet.createRow(0);
//        header.createCell(0).setCellValue("id");
//        header.createCell(1).setCellValue("이름");
//        header.createCell(2).setCellValue("학번");
//        header.createCell(3).setCellValue("연락처");
//        header.createCell(4).setCellValue("비교(임원진) - 영어만");
//        header.createCell(5).setCellValue("학과(부)");
//
//        Row row1 = sheet.createRow(1);
//        row1.createCell(0).setCellValue(1);
//        row1.createCell(1).setCellValue("5uhwann");
//        row1.createCell(2).setCellValue("60001234");
//        row1.createCell(3).setCellValue("010-1234-5678");
//        row1.createCell(4).setCellValue("LEADER");
//        row1.createCell(5).setCellValue("융합소프트웨어학부");
//
//        Row row2 = sheet.createRow(2);
//        row2.createCell(0).setCellValue(6);
//        row2.createCell(1).setCellValue("5uhwann");
//        row2.createCell(2).setCellValue(60001234);
//        row2.createCell(3).setCellValue("010-1234-5678");
//        row2.createCell(4).setCellValue("LEADER");
//        row2.createCell(5).setCellValue("융합소프트웨어학부");
//        workbook.write(out);
//        workbook.close();
//
//        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
//        MultipartFile validExcelFile = new MockMultipartFile(
//                "file",
//                "valid_excel.xlsx",
//                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
//                in
//        );
//
//        User savedUser = userRepository.save(fixtureMonkey.giveMeOne(User.class));
//        Club savedClub = clubRepository.save(
//                fixtureMonkey.giveMeBuilder(Club.class)
//                        .set("user", savedUser)
//                        .set("score", Score.from(0))
//                        .sample());
//        List<ClubMember> clubMembers = fixtureMonkey.giveMeBuilder(ClubMember.class)
//                .set("club", savedClub)
//                .sampleList(5);
//        clubMemberRepository.saveAll(clubMembers);
//
//        //when
//        facadeClubMemberService.updateMemberList(savedUser.getId(), validExcelFile);
//
//        //then
//        List<ClubMember> updatedClubMemberList = clubMemberRepository.findAll();
//        boolean has3To6Id = updatedClubMemberList.stream()
//                .anyMatch(cm -> cm.getId() >= 3 && cm.getId() <= 5);
//        assertThat(updatedClubMemberList.size()).isEqualTo(2);
//        assertThat(has3To6Id).isFalse();
//    }

    @DisplayName("동아리원 정보를 수정한다.")
    @Test
    void update() {
        //given
        User savedUser = userRepository.save(fixtureMonkey.giveMeOne(User.class));
        Club savedClub = clubRepository.save(
                fixtureMonkey.giveMeBuilder(Club.class)
                        .set("user", savedUser)
                        .set("clubMembers", null)
                        .set("score", Score.from(new BigDecimal(0)))
                        .sample());
        ClubMember savedClubMember = clubMemberRepository.save(
                fixtureMonkey.giveMeBuilder(ClubMember.class).set("club", savedClub).sample());

        UpdateClubMemberCommand updateClubMemberCommand = UpdateClubMemberCommand.builder()
                .name("test")
                .phoneNumber("010-1234-5678")
                .studentNumber("60001234")
                .position(Position.LEADER)
                .department("test").build();

        //when
        facadeClubMemberService.update(savedClubMember.getId(), updateClubMemberCommand);

        //then
        ClubMember updatedClubMember = clubMemberService.getById(savedClubMember.getId());
        assertThat(updatedClubMember.getName()).isEqualTo("test");
        assertThat(updatedClubMember.getPhoneNumber()).isEqualTo("010-1234-5678");
        assertThat(updatedClubMember.getStudentNumber()).isEqualTo("60001234");
        assertThat(updatedClubMember.getPosition()).isEqualTo(Position.LEADER);
        assertThat(updatedClubMember.getDepartment()).isEqualTo("test");
    }

}
