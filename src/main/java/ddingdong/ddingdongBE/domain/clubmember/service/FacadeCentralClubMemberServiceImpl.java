package ddingdong.ddingdongBE.domain.clubmember.service;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.clubmember.entity.ClubMember;
import ddingdong.ddingdongBE.domain.clubmember.service.dto.command.UpdateClubMemberCommand;
import ddingdong.ddingdongBE.domain.clubmember.service.dto.command.UpdateClubMemberListCommand;
import ddingdong.ddingdongBE.domain.clubmember.service.dto.query.AllClubMemberInfoQuery;
import ddingdong.ddingdongBE.file.service.ExcelFileService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FacadeCentralClubMemberServiceImpl implements FacadeCentralClubMemberService {

    private final ClubService clubService;
    private final ClubMemberService clubMemberService;
    private final ExcelFileService excelFileService;

    @Override
    public byte[] getClubMemberListFile(Long userId) {
        Club club = clubService.getByUserId(userId);
        return excelFileService.generateClubMemberListFile(club.getClubMembers());
    }

    @Override
    public AllClubMemberInfoQuery getAllMyClubMember(Long userId) {
        Club club = clubService.getByUserIdWithFetch(userId);
        return AllClubMemberInfoQuery.of(club.getName(), club.getClubMembers());
    }

    @Override
    @Transactional
    public void updateMemberList(UpdateClubMemberListCommand command) {
        Club club = clubService.getByUserId(command.userId());
        List<ClubMember> updatedClubMembers =
                excelFileService.extractClubMembersInformation(club, command.clubMemberListFile());
        List<ClubMember> clubMembers = club.getClubMembers();
        Set<Long> updatedMemberIds = updatedClubMembers.stream()
                .map(ClubMember::getId)
                .collect(Collectors.toSet());
        Set<Long> currentMemberIds = clubMembers.stream()
                .map(ClubMember::getId)
                .collect(Collectors.toSet());

        clubMemberService.saveAll(filterCreatedMembers(updatedClubMembers, updatedMemberIds, currentMemberIds));
        clubMemberService.deleteAll(filterDeletedMembers(clubMembers, updatedMemberIds, currentMemberIds));
    }

    @Override
    @Transactional
    public void update(UpdateClubMemberCommand command) {
        ClubMember clubMember = clubMemberService.getById(command.clubMemberId());
        clubMember.updateInformation(command.toEntity());
    }

    @Override
    @Transactional
    public void delete(final Long userId, final Long clubMemberId) {
        Club club = clubService.getByUserId(userId);
        ClubMember clubMember = clubMemberService.getById(clubMemberId);
        clubMember.validateBelongsToClub(club);
        clubMemberService.delete(clubMember);
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
