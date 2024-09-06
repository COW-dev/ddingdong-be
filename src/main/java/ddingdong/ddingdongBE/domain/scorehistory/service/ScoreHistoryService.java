package ddingdong.ddingdongBE.domain.scorehistory.service;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.scorehistory.controller.dto.request.CreateScoreHistoryRequest;
import ddingdong.ddingdongBE.domain.scorehistory.entity.ScoreHistory;
import ddingdong.ddingdongBE.domain.scorehistory.repository.ScoreHistoryRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ScoreHistoryService {

    private final ScoreHistoryRepository scoreHistoryRepository;
    private final ClubService clubService;

    public void create(final Long clubId, CreateScoreHistoryRequest createScoreHistoryRequest) {
        Club club = clubService.getByClubId(clubId);

        BigDecimal score = roundToThirdPoint(createScoreHistoryRequest.amount());
        clubService.updateClubScore(clubId, score);
        scoreHistoryRepository.save(createScoreHistoryRequest.toEntity(club));
    }

    @Transactional(readOnly = true)
    public List<ScoreHistory> findAllByClubId(final Long clubId) {
        return scoreHistoryRepository.findByClubId(clubId);
    }

    @Transactional(readOnly = true)
    public List<ScoreHistory> findAllByUserId(final Long userId) {
        Club club = clubService.getByUserId(userId);
        return scoreHistoryRepository.findByClubId(club.getId());
    }

    private BigDecimal roundToThirdPoint(BigDecimal value) {
        return value.setScale(3, RoundingMode.DOWN);
    }
}
