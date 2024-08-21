package ddingdong.ddingdongBE.file.service;

import ddingdong.ddingdongBE.common.exception.ParsingExcelFileException.ExcelIO;
import ddingdong.ddingdongBE.common.exception.ParsingExcelFileException.NonExcelFile;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.ClubMember;
import ddingdong.ddingdongBE.file.dto.ExcelClubMemberDto;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ExcelFileService {

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

}
