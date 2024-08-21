package ddingdong.ddingdongBE.file.dto;

import ddingdong.ddingdongBE.common.exception.ParsingExcelFileException.NonValidatedStringCellValue;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.ClubMember;
import ddingdong.ddingdongBE.domain.club.entity.Position;
import java.util.Arrays;
import java.util.Iterator;
import lombok.Builder;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

@Getter
public class ExcelClubMemberDto {

    private static final DataFormatter formatter = new DataFormatter();


    private String name;

    private String studentNumber;

    private String phoneNumber;

    private String position;

    private String department;

    @Builder
    private ExcelClubMemberDto(String name, String studentNumber, String phoneNumber, String position,
                               String department) {
        this.name = name;
        this.studentNumber = studentNumber;
        this.phoneNumber = phoneNumber;
        this.position = position;
        this.department = department;
    }


    public ClubMember toEntity(Club club) {
        return ClubMember.builder()
                .club(club)
                .name(name)
                .studentNumber(studentNumber)
                .phoneNumber(phoneNumber)
                .position(Position.valueOf(position))
                .department(department).build();
    }

    public static ExcelClubMemberDto fromExcelRow(Row row) {
        ExcelClubMemberDto clubMemberDto = ExcelClubMemberDto.builder().build();
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            if (cell.getCellType() == CellType.STRING) {
                if (cell.getStringCellValue() != null) {
                    clubMemberDto.setValueByCell(cell.getStringCellValue(), cell.getColumnIndex());
                }
            } else if (cell.getCellType() == CellType.NUMERIC) {
                if (cell.getNumericCellValue() != 0) {
                    String stringCellValue = formatter.formatCellValue(cell);
                    clubMemberDto.setValueByCell(stringCellValue, cell.getColumnIndex());
                }
            }
        }
        return clubMemberDto;
    }

    private void setValueByCell(String stringCellValue, int columnIndex) {
        switch (columnIndex) {
            case 0 -> this.name = stringCellValue;
            case 1 -> this.studentNumber = stringCellValue;
            case 2 -> this.phoneNumber = stringCellValue;
            case 3 -> {
                validatePositionValue(stringCellValue);
                this.position = stringCellValue;
            }
            case 4 -> this.department = stringCellValue;
        }
    }

    private void validatePositionValue(String stringCellValue) {
        if (Arrays.stream(Position.values()).noneMatch(position -> position.name().equals(stringCellValue))) {
            throw new NonValidatedStringCellValue("동아리원의 역할은 LEADER, EXECUTIVE, MEMBER 중 하나입니다.");
        }
    }
}
