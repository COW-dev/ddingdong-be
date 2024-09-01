package ddingdong.ddingdongBE.domain.club.service;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.club.entity.ClubMember;
import ddingdong.ddingdongBE.domain.club.repository.ClubMemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClubMemberService {

    private final ClubMemberRepository clubMemberRepository;

    public ClubMember getById(Long clubMemberId) {
        return clubMemberRepository.findById(clubMemberId)
            .orElseThrow(() -> new ResourceNotFound("존재하지 않는 동아리원입니다."));
    }

    @Transactional
    public void saveAll(List<ClubMember> clubMembers) {
        clubMemberRepository.saveAll(clubMembers);
    }

    @Transactional
    public void deleteAll(List<ClubMember> clubMembers) {
        clubMemberRepository.deleteAllInBatch(clubMembers);
    }

}
