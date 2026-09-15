package ddingdong.ddingdongBE.domain.activityreport.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import ddingdong.ddingdongBE.common.fixture.ActivityReportTermInfoFixture;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.activityreport.entity.ActivityReportTermStatus;
import ddingdong.ddingdongBE.domain.activityreport.repository.ActivityReportTermInfoRepository;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportTermInfoQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FacadeClubActivityReportServiceImplTest extends TestContainerSupport {

    @Autowired
    private FacadeClubActivityReportService facadeClubActivityReportService;
    @Autowired
    private ActivityReportTermInfoRepository activityReportTermInfoRepository;

    @BeforeEach
    void setUp() {
        activityReportTermInfoRepository.saveAll(List.of(
                ActivityReportTermInfoFixture.create(1, LocalDate.of(2026, 3, 2), LocalDate.of(2026, 3, 15)),
                ActivityReportTermInfoFixture.create(2, LocalDate.of(2026, 3, 16), LocalDate.of(2026, 3, 29)),
                ActivityReportTermInfoFixture.create(3, LocalDate.of(2026, 4, 13), LocalDate.of(2026, 4, 26))
        ));
    }

    @DisplayName("어느 회차 기간에도 속하지 않는 공백 기간에는 지난 회차는 진행 종료, 이후 회차는 진행 전이다")
    @Test
    void statusDuringGapBetweenTerms() {
        // given
        LocalDateTime gapPeriod = LocalDateTime.of(2026, 4, 5, 12, 0);

        // when
        List<ActivityReportTermInfoQuery> termInfos = facadeClubActivityReportService.getActivityReportTermInfos(gapPeriod);

        // then
        assertThat(termInfos)
                .extracting(ActivityReportTermInfoQuery::term, ActivityReportTermInfoQuery::status)
                .containsExactlyInAnyOrder(
                        tuple(1, ActivityReportTermStatus.CLOSED),
                        tuple(2, ActivityReportTermStatus.CLOSED),
                        tuple(3, ActivityReportTermStatus.UPCOMING)
                );
    }

    @DisplayName("진행 중인 회차가 있으면 이전 회차는 진행 종료, 해당 회차는 진행 중, 이후 회차는 진행 전이다")
    @Test
    void statusDuringOngoingTerm() {
        // given
        LocalDateTime ongoingPeriod = LocalDateTime.of(2026, 3, 20, 12, 0);

        // when
        List<ActivityReportTermInfoQuery> termInfos = facadeClubActivityReportService.getActivityReportTermInfos(ongoingPeriod);

        // then
        assertThat(termInfos)
                .extracting(ActivityReportTermInfoQuery::term, ActivityReportTermInfoQuery::status)
                .containsExactlyInAnyOrder(
                        tuple(1, ActivityReportTermStatus.CLOSED),
                        tuple(2, ActivityReportTermStatus.ONGOING),
                        tuple(3, ActivityReportTermStatus.UPCOMING)
                );
    }
}