package ddingdong.ddingdongBE.domain.scorehistory.service;

import ddingdong.ddingdongBE.domain.scorehistory.entity.ScoreHistory;
import ddingdong.ddingdongBE.domain.scorehistory.repository.ScoreHistoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeneralScoreHistoryService implements ScoreHistoryService {

    private final ScoreHistoryRepository scoreHistoryRepository;

    @Override
    @Transactional
    public Long create(ScoreHistory scoreHistory) {
        ScoreHistory savedScoreHistory = scoreHistoryRepository.save(scoreHistory);
        return savedScoreHistory.getId();
    }

    @Override
    public List<ScoreHistory> findAllByClubId(Long clubId) {
        return scoreHistoryRepository.findByClubId(clubId);
    }
}
