package ddingdong.ddingdongBE.domain.club.service;

import static ddingdong.ddingdongBE.common.exception.ErrorMessage.NO_SUCH_CLUB;

import ddingdong.ddingdongBE.domain.club.controller.dto.request.UpdateClubMemberRequest;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.ClubMember;
import ddingdong.ddingdongBE.domain.club.repository.ClubMemberRepository;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.file.service.ExcelFileService;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class ClubMemberService {

    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final ExcelFileService excelFileService;

    public void updateClubMembers(Long userId, UpdateClubMemberRequest request,
                                  Optional<MultipartFile> clubMemberListFile) {
        Club club = clubRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException(NO_SUCH_CLUB.getText()));

        List<ClubMember> requestedClubMembers;
        if (clubMemberListFile.isPresent()) {
            MultipartFile file = clubMemberListFile.get();
            requestedClubMembers = excelFileService.extractClubMembersInformation(club, file);
        } else {
            requestedClubMembers = request.getClubMemberList().stream()
                    .map(clubMemberDto -> clubMemberDto.toEntity(club))
                    .toList();
        }

        clubMemberRepository.deleteAllByClub(club);
        clubMemberRepository.saveAll(requestedClubMembers);
    }
}
