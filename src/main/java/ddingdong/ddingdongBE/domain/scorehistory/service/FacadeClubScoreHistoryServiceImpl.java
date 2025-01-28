package ddingdong.ddingdongBE.domain.scorehistory.service;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.scorehistory.entity.ScoreHistory;
import ddingdong.ddingdongBE.domain.scorehistory.service.dto.query.ClubScoreHistoryListQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacadeClubScoreHistoryServiceImpl implements FacadeClubScoreHistoryService {

    private final ScoreHistoryService scoreHistoryService;
    private final ClubService clubService;

    @Override
    public ClubScoreHistoryListQuery findMyScoreHistories(Long userId) {
        Club club = clubService.getByUserId(userId);
        List<ScoreHistory> scoreHistories = scoreHistoryService.findAllByClubId(club.getId());
        return ClubScoreHistoryListQuery.of(club.getScore().getValue(), scoreHistories);
    }
}
