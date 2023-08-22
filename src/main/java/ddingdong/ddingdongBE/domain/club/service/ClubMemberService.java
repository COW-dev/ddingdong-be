package ddingdong.ddingdongBE.domain.club.service;

import static ddingdong.ddingdongBE.common.exception.ErrorMessage.*;

import ddingdong.ddingdongBE.domain.club.controller.dto.request.ClubMemberDto;
import ddingdong.ddingdongBE.domain.club.controller.dto.request.UpdateClubMemberRequest;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.ClubMember;
import ddingdong.ddingdongBE.domain.club.repository.ClubMemberRepository;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ClubMemberService {

    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;

    public void updateClubMembers(Long userId, UpdateClubMemberRequest request) {
        Club club = clubRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException(NO_SUCH_CLUB.getText()));

        List<ClubMember> clubMembers = clubMemberRepository.findAll();
        if (!clubMembers.isEmpty()) {
            enrollClubMembers(request, clubMembers, club);
            deleteClubMembers(request, clubMembers);
            return;
        }
        List<ClubMember> requestClubMemberList = request.getClubMemberList().stream()
                .map(clubMemberDto -> clubMemberDto.toEntity(club))
                .toList();
        clubMemberRepository.saveAll(requestClubMemberList);
    }

    private void deleteClubMembers(UpdateClubMemberRequest request, List<ClubMember> clubMembers) {
        List<String> requestUpdateClubMemberStudentNumbers = request.getClubMemberList().stream()
                .map(ClubMemberDto::getStudentNumber)
                .toList();
        List<ClubMember> deleteClubMembers = clubMembers.stream()
                .filter(clubMember -> !requestUpdateClubMemberStudentNumbers.contains(clubMember.getStudentNumber()))
                .toList();
        clubMemberRepository.deleteAll(deleteClubMembers);
    }

    private void enrollClubMembers(UpdateClubMemberRequest request, List<ClubMember> clubMembers, Club club) {
        List<String> clubMemberStudentNumbers = clubMembers.stream()
                .map(ClubMember::getStudentNumber)
                .toList();
        List<ClubMember> enrollClubMembers = request.getClubMemberList().stream()
                .filter(clubMemberDto -> !clubMemberStudentNumbers.contains(clubMemberDto.getStudentNumber()))
                .map(clubMemberDto -> clubMemberDto.toEntity(club))
                .toList();
        clubMemberRepository.saveAll(enrollClubMembers);
    }

}
