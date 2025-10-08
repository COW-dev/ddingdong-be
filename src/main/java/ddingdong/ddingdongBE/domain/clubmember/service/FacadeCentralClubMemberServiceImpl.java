package ddingdong.ddingdongBE.domain.clubmember.service;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.clubmember.entity.ClubMember;
import ddingdong.ddingdongBE.domain.clubmember.service.dto.command.CreateClubMemberCommand;
import ddingdong.ddingdongBE.domain.clubmember.service.dto.command.UpdateClubMemberCommand;
import ddingdong.ddingdongBE.domain.clubmember.service.dto.command.UpdateClubMemberListCommand;
import ddingdong.ddingdongBE.domain.clubmember.service.dto.query.AllClubMemberInfoQuery;
import ddingdong.ddingdongBE.file.service.ExcelFileService;
import java.util.List;
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
        List<ClubMember> updateClubMember =
                excelFileService.extractClubMembersInformation(club, command.clubMemberListFile());
        clubMemberService.deleteAllByClubId(club.getId());
        clubMemberService.saveAll(updateClubMember);
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
}
