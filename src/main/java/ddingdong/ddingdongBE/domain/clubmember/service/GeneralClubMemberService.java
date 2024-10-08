package ddingdong.ddingdongBE.domain.clubmember.service;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.clubmember.entity.ClubMember;
import ddingdong.ddingdongBE.domain.clubmember.repository.ClubMemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GeneralClubMemberService implements ClubMemberService {

    private final ClubMemberRepository clubMemberRepository;

    @Override
    public ClubMember getById(Long clubMemberId) {
        return clubMemberRepository.findById(clubMemberId)
                .orElseThrow(() -> new ResourceNotFound("존재하지 않는 동아리원입니다."));
    }

    @Override
    @Transactional
    public void saveAll(List<ClubMember> clubMembers) {
        clubMemberRepository.saveAll(clubMembers);
    }

    @Override
    @Transactional
    public void deleteAll(List<ClubMember> clubMembers) {
        clubMemberRepository.deleteAllInBatch(clubMembers);
    }

}
