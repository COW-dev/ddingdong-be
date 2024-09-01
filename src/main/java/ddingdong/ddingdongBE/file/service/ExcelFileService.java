package ddingdong.ddingdongBE.file.service;

import ddingdong.ddingdongBE.common.exception.ParsingExcelFileException.ExcelIO;
import ddingdong.ddingdongBE.common.exception.ParsingExcelFileException.NonExcelFile;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.ClubMember;
import ddingdong.ddingdongBE.file.dto.ExcelClubMemberDto;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ExcelFileService {

    private static final String CLUB_MEMBER_EXCEL_MANUAL_IMAGE_PATH = "src/main/resources/static/club_member_excel_menual.png";

    public byte[] generateClubMemberListFile(List<ClubMember> clubMembers) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("동아리원 명단");
            sheet.setZoom(125);
            createHeaderRow(workbook, sheet);
            createDataRow(clubMembers, sheet);
            addManualImage(workbook, sheet);

            for (int i = 0; i < 13; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate Excel file", e);
        }
    }

    public List<ClubMember> extractClubMembersInformation(Club club, MultipartFile file) {
        isExcelFile(file);
        List<ExcelClubMemberDto> requestedClubMembersDto = parsingClubMemberListFile(file);
        return requestedClubMembersDto.stream()
                .map(clubMemberDto -> clubMemberDto.toEntity(club))
                .toList();
    }

    private void isExcelFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName != null && !(fileName.endsWith(".xls") || fileName.endsWith(".xlsx"))) {
            throw new NonExcelFile();
        }
    }

    private List<ExcelClubMemberDto> parsingClubMemberListFile(MultipartFile clubMemberListFile) {
        List<ExcelClubMemberDto> requestedClubMembersDto = new ArrayList<>();
        try {
            Workbook workbook = new XSSFWorkbook(clubMemberListFile.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() != 0 && row.getCell(row.getFirstCellNum()).getCellType() != CellType.BLANK) {
                    requestedClubMembersDto.add(ExcelClubMemberDto.fromExcelRow(row));
                }
            }
        } catch (IOException | NotOfficeXmlFileException e) {
            throw new ExcelIO();
        }
        return requestedClubMembersDto;
    }


    private void createHeaderRow(Workbook workbook, Sheet sheet) {
        XSSFCellStyle headerCellStyle = (XSSFCellStyle) workbook.createCellStyle();
        XSSFColor myColor = new XSSFColor(new Color(177, 207, 149), null);
        headerCellStyle.setFillForegroundColor(myColor);
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerCellStyle.setFont(headerFont);

        Row headerRow = sheet.createRow(0);
        String[] headers = {"식별자(수정X)", "이름", "학번", "연락처", "비교(임원진) - 영어만", "학과(부)"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerCellStyle);
        }

    }

    private void createDataRow(List<ClubMember> clubMembers, Sheet sheet) {
        int rowNum = 1;
        for (ClubMember clubMember : clubMembers) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(clubMember.getId());
            row.createCell(1).setCellValue(clubMember.getName());
            row.createCell(2).setCellValue(clubMember.getStudentNumber());
            row.createCell(3).setCellValue(clubMember.getPhoneNumber());
            row.createCell(4).setCellValue(clubMember.getPosition().name());
            row.createCell(5).setCellValue(clubMember.getDepartment());
        }
    }

    private void addManualImage(Workbook workbook, Sheet sheet) throws IOException {
        InputStream inputStream = new FileInputStream(CLUB_MEMBER_EXCEL_MANUAL_IMAGE_PATH);
        byte[] bytes = IOUtils.toByteArray(inputStream);
        int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
        inputStream.close();

        CreationHelper helper = workbook.getCreationHelper();
        Drawing<?> drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = helper.createClientAnchor();

        anchor.setCol1(8);
        anchor.setRow1(0);
        anchor.setCol2(anchor.getCol1() + 7);
        anchor.setRow2(26);

        drawing.createPicture(anchor, pictureIdx);
    }
}
