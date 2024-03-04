package ddingdong.ddingdongBE.domain.club.service;

import static ddingdong.ddingdongBE.common.exception.ErrorMessage.NO_SUCH_CLUB;

import ddingdong.ddingdongBE.domain.club.controller.dto.request.ClubMemberDto;
import ddingdong.ddingdongBE.domain.club.controller.dto.request.UpdateClubMemberRequest;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.ClubMember;
import ddingdong.ddingdongBE.domain.club.repository.ClubMemberRepository;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class ClubMemberService {

    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;

    public void updateClubMembers(Long userId, UpdateClubMemberRequest request,
                                  Optional<MultipartFile> clubMemberListFile) {
        if (clubMemberListFile.isPresent()) {
            MultipartFile file = clubMemberListFile.get();
            isExcelFile(file);

            Club club = clubRepository.findByUserId(userId)
                    .orElseThrow(() -> new IllegalArgumentException(NO_SUCH_CLUB.getText()));
            List<ClubMemberDto> requestedClubMemberDtos = parsingClubMemberListFile(file);
            List<ClubMember> requestedClubMembers = requestedClubMemberDtos.stream()
                    .map(clubMemberDto -> clubMemberDto.toEntity(club))
                    .toList();

            clubMemberRepository.deleteAllByClub(club);
            clubMemberRepository.saveAll(requestedClubMembers);
            return;
        }

        Club club = clubRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException(NO_SUCH_CLUB.getText()));

        List<Long> memberIds = clubMemberRepository.findClubMembersByClubId(club.getId())
                .stream()
                .map(ClubMember::getId)
                .toList();

        List<ClubMember> requestedClubMembers = request.getClubMemberList().stream()
                .map(clubMemberDto -> clubMemberDto.toEntity(club))
                .toList();

        if (!memberIds.isEmpty()) {
            clubMemberRepository.deleteAllById(memberIds);
        }

        clubMemberRepository.saveAll(requestedClubMembers);
    }


    private void isExcelFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName != null && !(fileName.endsWith(".xls") || fileName.endsWith(".xlsx"))) {
            throw new IllegalArgumentException("엑셀파일이 아닙니다.");
        }
    }

    private static List<ClubMemberDto> parsingClubMemberListFile(MultipartFile clubMemberListFile) {
        List<ClubMemberDto> requestedClubMemberDtos = new ArrayList<>();
        try {
            Workbook workbook = new XSSFWorkbook(clubMemberListFile.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() != 0 && row.getCell(row.getFirstCellNum()).getCellType() != CellType.BLANK) {
                    requestedClubMemberDtos.add(ClubMemberDto.fromExcelRow(row));
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("올바른 엑셀 양식을 사용해주세요.");
        }
        return requestedClubMemberDtos;
    }
}
