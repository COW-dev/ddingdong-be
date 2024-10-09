package ddingdong.ddingdongBE.file.service;

import static ddingdong.ddingdongBE.common.exception.ParsingExcelFileException.ExcelIO;
import static ddingdong.ddingdongBE.domain.club.entity.Position.LEADER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.exception.InvalidatedMappingException.InvalidatedEnumValue;
import ddingdong.ddingdongBE.common.exception.ParsingExcelFileException;
import ddingdong.ddingdongBE.common.exception.ParsingExcelFileException.NonExcelFile;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.clubmember.entity.ClubMember;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
class ExcelFileServiceTest extends TestContainerSupport {

    @Autowired
    private ExcelFileService excelFileService;


    @Test
    @DisplayName("엑셀 파일을 정상적으로 파싱하는 경우")
    void extractClubMembersInformationWithValidatedExcelFile() throws IOException {
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
        row2.createCell(0).setCellValue(2);
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

        Club club = Club.builder().id(1L).build();

        // when
        List<ClubMember> clubMembers = excelFileService.extractClubMembersInformation(club, validExcelFile);

        // then
        assertThat(clubMembers).hasSize(2)
                .extracting("club", "name", "studentNumber", "phoneNumber", "position", "department")
                .contains(tuple(club, "5uhwann", "60001234", "010-1234-5678", LEADER, "융합소프트웨어학부"));
    }

    @DisplayName("엑셀파일이 아닌 경우 예외가 발생한다.")
    @Test
    void extractClubMembersInformationWithNonExcelFile() {
        //given
        Club club = Club.builder().id(1L).build();
        MultipartFile nonExcelFile = new MockMultipartFile(
                "file",
                "not_excel.txt",
                "text/plain",
                "some data".getBytes()
        );

        //when //then
        assertThatThrownBy(() -> excelFileService.extractClubMembersInformation(club, nonExcelFile))
                .isInstanceOf(NonExcelFile.class)
                .hasMessage(ParsingExcelFileException.NON_EXCEL_FILE_ERROR_MESSAGE);
    }


    @Test
    @DisplayName("엑셀 파일 파싱 중 IO 예외 발생")
    void extractClubMembersInformationWithIOException() throws IOException {
        // given
        byte[] invalidContent = new byte[]{0x00, 0x01, 0x02}; // 잘못된 데이터
        MultipartFile invalidExcelFile = new MockMultipartFile(
                "file",
                "invalid_excel.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                new ByteArrayInputStream(invalidContent)
        );
        Club club = Club.builder().id(1L).build();

        // when
        assertThatThrownBy(() -> excelFileService.extractClubMembersInformation(club, invalidExcelFile))
                .isInstanceOf(ExcelIO.class)
                .hasMessage(ParsingExcelFileException.EXCEL_IO_ERROR_MESSAGE);
    }

    @DisplayName("동아리원 명단 엑셀 파일에서 올바른 동아리원 역할(LEADER, EXECUTION, MEMBER)이 아닐 경우 예외가 발생한다.")
    @Test
    void extractClubMembersInformationWithNonValidatedStringCellValue() throws IOException {
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
        row1.createCell(4).setCellValue("member");
        row1.createCell(5).setCellValue("융합소프트웨어학부");

        workbook.write(out);
        workbook.close();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        MultipartFile nonValidExcelFile = new MockMultipartFile(
                "file",
                "valid_excel.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                in
        );

        Club club = Club.builder().id(1L).build();

        //when //then
        assertThatThrownBy(() -> excelFileService.extractClubMembersInformation(club, nonValidExcelFile))
                .isInstanceOf(InvalidatedEnumValue.class)
                .hasMessage("동아리원의 역할은 LEADER, EXECUTIVE, MEMBER 중 하나입니다.");
    }
}
