package ddingdong.ddingdongBE.domain.scorehistory.service;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.scorehistory.controller.dto.request.RegisterScoreRequest;
import ddingdong.ddingdongBE.domain.scorehistory.controller.dto.response.ScoreHistoryFilterByClubResponse;
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

    public void register(final Long clubId, RegisterScoreRequest registerScoreRequest) {
        Club club = clubService.findClubByClubId(clubId);

        float score = roundToThirdPoint(registerScoreRequest.getAmount());

        float remainingScore = clubService.editClubScore(clubId, score);

        scoreHistoryRepository.save(registerScoreRequest.toEntity(club, remainingScore));
    }

    @Transactional(readOnly = true)
    public List<ScoreHistoryFilterByClubResponse> getScoreHistories(final Long clubId) {

        return scoreHistoryRepository.findByClubId(clubId).stream()
                .map(ScoreHistoryFilterByClubResponse::of)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ScoreHistoryFilterByClubResponse> getMyScoreHistories(final Long userId) {
        Club club = clubService.findClubByUserId(userId);

        return scoreHistoryRepository.findByClubId(club.getId()).stream()
                .map(ScoreHistoryFilterByClubResponse::of)
                .toList();
    }

    private static float roundToThirdPoint(float value) {
        return Math.round(value * 1000.0) / 1000.0F;
    }
}