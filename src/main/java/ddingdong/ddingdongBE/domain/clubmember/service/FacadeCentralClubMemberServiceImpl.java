package ddingdong.ddingdongBE.domain.clubmember.service;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.clubmember.entity.ClubMember;
import ddingdong.ddingdongBE.domain.clubmember.service.dto.command.CreateClubMemberCommand;
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
        List<ClubMember> updateClubMemberInfos =
                excelFileService.extractClubMembersInformation(club, command.clubMemberListFile());
        List<ClubMember> clubMembers = clubMemberService.getByClubId(club.getId());
        Set<Long> updatedMemberInfoIds = updateClubMemberInfos.stream()
                .map(ClubMember::getId)
                .collect(Collectors.toSet());
        Set<Long> currentMemberIds = clubMembers.stream()
                .map(ClubMember::getId)
                .collect(Collectors.toSet());

        clubMemberService.saveAll(filterCreatedMembers(updateClubMemberInfos, updatedMemberInfoIds, currentMemberIds));
        clubMemberService.updateAll(filterUpdatedMembers(updateClubMemberInfos, updatedMemberInfoIds, currentMemberIds));

        List<ClubMember> deletedMembers = filterDeletedMembers(clubMembers, updatedMemberInfoIds, currentMemberIds);
        club.removeAll(deletedMembers);
        clubMemberService.deleteAll(deletedMembers);
    }

    @Override
    @Transactional
    public void update(UpdateClubMemberCommand command) {
        ClubMember clubMember = clubMemberService.getById(command.clubMemberId());
        clubMember.updateInformation(command.toEntity());
    }

    @Override
    @Transactional
    public void delete(Long userId, Long clubMemberId) {
        Club club = clubService.getByUserId(userId);
        ClubMember clubMember = clubMemberService.getById(clubMemberId);
        clubMember.validateBelongsToClub(club);
        clubMemberService.delete(clubMember);
    }

    @Override
    @Transactional
    public void create(final CreateClubMemberCommand command) {
        Club club = clubService.getByUserId(command.userId());
        ClubMember clubMember = command.toEntity(club);
        clubMemberService.save(clubMember);
        club.addClubMember(clubMember);
    }

    private List<ClubMember> filterCreatedMembers(List<ClubMember> updatedClubMembers, Set<Long> updatedMemberIds,
                                                  Set<Long> currentMemberIds) {
        Set<Long> createdMemberIds = new HashSet<>(updatedMemberIds);
        createdMemberIds.removeAll(currentMemberIds);
        return updatedClubMembers.stream()
                .filter(member -> createdMemberIds.contains(member.getId()))
                .toList();
    }

    private List<ClubMember> filterUpdatedMembers(List<ClubMember> updatedClubMembers, Set<Long> updatedMemberIds,
            Set<Long> currentMemberIds) {
        Set<Long> willUpdateMemberIds = new HashSet<>(currentMemberIds);
        willUpdateMemberIds.retainAll(updatedMemberIds);
        return updatedClubMembers.stream()
                .filter(member -> willUpdateMemberIds.contains(member.getId()))
                .toList();
    }

    private List<ClubMember> filterDeletedMembers(List<ClubMember> clubMembers, Set<Long> updatedMemberIds,
                                                  Set<Long> currentMemberIds) {
        Set<Long> deletedMemberIds = new HashSet<>(currentMemberIds);
        deletedMemberIds.removeAll(updatedMemberIds);
        System.out.println(deletedMemberIds);
        return clubMembers.stream()
                .filter(member -> deletedMemberIds.contains(member.getId()))
                .toList();
    }
}
