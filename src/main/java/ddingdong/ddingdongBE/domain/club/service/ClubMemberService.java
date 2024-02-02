package ddingdong.ddingdongBE.domain.club.service;

import static ddingdong.ddingdongBE.common.exception.ErrorMessage.*;

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
}