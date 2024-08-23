package ddingdong.ddingdongBE.domain.club.service;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.ClubMember;
import ddingdong.ddingdongBE.domain.club.repository.ClubMemberRepository;
import ddingdong.ddingdongBE.file.service.ExcelFileService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class ClubMemberService {

    private final ClubService clubService;
    private final ClubMemberRepository clubMemberRepository;
    private final ExcelFileService excelFileService;

    public void updateMemberList(Long userId, MultipartFile clubMemberListFile) {
        Club club = clubService.getByUserId(userId);
        List<ClubMember> updatedClubMembers = excelFileService.extractClubMembersInformation(club, clubMemberListFile);
        List<ClubMember> clubMembers = club.getClubMembers();
        Set<Long> updatedMemberIds = updatedClubMembers.stream()
                .map(ClubMember::getId)
                .collect(Collectors.toSet());
        Set<Long> currentMemberIds = clubMembers.stream()
                .map(ClubMember::getId)
                .collect(Collectors.toSet());

        clubMemberRepository.saveAll(filterCreatedMembers(updatedClubMembers, updatedMemberIds, currentMemberIds));
        clubMemberRepository.deleteAll(filterDeletedMembers(clubMembers, updatedMemberIds, currentMemberIds));
    }

    private List<ClubMember> filterCreatedMembers(List<ClubMember> updatedClubMembers, Set<Long> updatedMemberIds,
                                                  Set<Long> currentMemberIds) {
        Set<Long> createdMemberIds = new HashSet<>(updatedMemberIds);
        createdMemberIds.removeAll(currentMemberIds);
        return updatedClubMembers.stream()
                .filter(member -> createdMemberIds.contains(member.getId()))
                .toList();
    }

    private List<ClubMember> filterDeletedMembers(List<ClubMember> clubMembers, Set<Long> updatedMemberIds,
                                                  Set<Long> currentMemberIds) {
        Set<Long> deletedMemberIds = new HashSet<>(currentMemberIds);
        deletedMemberIds.removeAll(updatedMemberIds);
        return clubMembers.stream()
                .filter(member -> deletedMemberIds.contains(member.getId()))
                .toList();
    }
}
