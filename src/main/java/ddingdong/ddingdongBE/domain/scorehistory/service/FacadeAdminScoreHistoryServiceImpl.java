package ddingdong.ddingdongBE.domain.scorehistory.service;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.scorehistory.entity.ScoreHistory;
import ddingdong.ddingdongBE.domain.scorehistory.service.dto.command.CreateScoreHistoryCommand;
import ddingdong.ddingdongBE.domain.scorehistory.service.dto.query.AdminClubScoreHistoryListQuery;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacadeAdminScoreHistoryServiceImpl implements FacadeAdminScoreHistoryService {

    private final ScoreHistoryService scoreHistoryService;
    private final ClubService clubService;

    @Override
    @Transactional
    public Long create(CreateScoreHistoryCommand command) {
        BigDecimal score = roundToThirdPoint(command.amount());
        Club club = clubService.getByClubId(command.clubId());
        clubService.updateClubScore(club.getId(), score);
        return scoreHistoryService.create(command.toEntity(club));
    }

    @Override
    public AdminClubScoreHistoryListQuery findAllByClubId(Long clubId) {
        Club club = clubService.getByClubId(clubId);
        List<ScoreHistory> scoreHistories = scoreHistoryService.findAllByClubId(clubId);
        return AdminClubScoreHistoryListQuery.of(club, scoreHistories);
    }

    private BigDecimal roundToThirdPoint(BigDecimal value) {
        return value.setScale(3, RoundingMode.DOWN);
    }

}
