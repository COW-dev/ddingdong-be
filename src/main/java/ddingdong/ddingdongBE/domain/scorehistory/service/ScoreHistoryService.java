package ddingdong.ddingdongBE.domain.scorehistory.service;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.scorehistory.controller.dto.request.CreateScoreHistoryRequest;
import ddingdong.ddingdongBE.domain.scorehistory.entity.ScoreHistory;
import ddingdong.ddingdongBE.domain.scorehistory.repository.ScoreHistoryRepository;
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

    public void register(final Long clubId, CreateScoreHistoryRequest createScoreHistoryRequest) {
        Club club = clubService.getByClubId(clubId);

        float score = roundToThirdPoint(createScoreHistoryRequest.amount());
        clubService.editClubScore(clubId, score);
        scoreHistoryRepository.save(createScoreHistoryRequest.toEntity(club));
    }

    @Transactional(readOnly = true)
    public List<ScoreHistory> getScoreHistories(final Long clubId) {

        return scoreHistoryRepository.findByClubId(clubId);
    }

    @Transactional(readOnly = true)
    public List<ScoreHistory> getMyScoreHistories(final Long userId) {
        Club club = clubService.getByUserId(userId);
        return scoreHistoryRepository.findByClubId(club.getId());
    }

    private static float roundToThirdPoint(float value) {
        return Math.round(value * 1000.0) / 1000.0F;
    }
}
