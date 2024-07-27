package ddingdong.ddingdongBE.domain.scorehistory.controller;

import static ddingdong.ddingdongBE.domain.scorehistory.entity.ScoreCategory.ACTIVITY_REPORT;
import static ddingdong.ddingdongBE.domain.scorehistory.entity.ScoreCategory.CARRYOVER_SCORE;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.scorehistory.entity.Score;
import ddingdong.ddingdongBE.domain.scorehistory.entity.ScoreHistory;
import ddingdong.ddingdongBE.support.WebApiTestSupport;
import ddingdong.ddingdongBE.support.WithMockAuthenticatedUser;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AdminScoreHistoryControllerTest extends WebApiTestSupport {

    @WithMockAuthenticatedUser(role = "ADMIN")
    @DisplayName("동아리 점수 내역 조회 요청을 수행한다.")
    @Test
    void getScoreHistories() throws Exception {
        //given
        Club club = Club.builder()
                .id(1L)
                .score(Score.of(55)).build();
        List<ScoreHistory> scoreHistories = List.of(ScoreHistory.builder()
                        .club(club)
                        .scoreCategory(CARRYOVER_SCORE)
                        .amount(5)
                        .reason("reasonA").build(),
                ScoreHistory.builder()
                        .club(club)
                        .scoreCategory(ACTIVITY_REPORT)
                        .amount(5)
                        .reason("reasonB").build());
        when(clubService.getByClubId(anyLong())).thenReturn(club);
        when(scoreHistoryService.getScoreHistories(club.getId())).thenReturn(scoreHistories);

        //when //then
        mockMvc.perform(get("/server/admin/{clubId}/score", 1L)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalScore").value(55))
                .andExpect(jsonPath("$.scoreHistories", hasSize(scoreHistories.size())))
                .andExpect(jsonPath("$.scoreHistories[0].scoreCategory").value(CARRYOVER_SCORE.getCategory()))
                .andExpect(jsonPath("$.scoreHistories[0].reason").value("reasonA"))
                .andExpect(jsonPath("$.scoreHistories[0].amount").value(5));
    }
}
