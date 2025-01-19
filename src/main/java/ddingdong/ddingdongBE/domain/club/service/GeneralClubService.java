package ddingdong.ddingdongBE.domain.club.service;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GeneralClubService implements ClubService {

    private final ClubRepository clubRepository;

    @Override
    @Transactional
    public Long save(Club club) {
        Club savedClub = clubRepository.save(club);
        return savedClub.getId();
    }

    @Override
    public Club getById(Long clubId) {
        return clubRepository.findById(clubId)
                .orElseThrow(() -> new ResourceNotFound("Club(clubId=" + clubId + ")를 찾을 수 없습니다."));
    }

    @Override
    public Club getByUserId(Long userId) {
        return clubRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFound("Club(userId=" + userId + ")를 찾을 수 없습니다."));
    }

    @Override
    public List<Club> findAll() {
        return clubRepository.findAll();
    }

    @Override
    @Transactional
    public void update(Club club, Club updatedClub) {
        club.updateClubInfo(updatedClub);
    }

    @Override
    @Transactional
    public void delete(Long clubId) {
        Club club = getById(clubId);
        clubRepository.delete(club);
    }

}
