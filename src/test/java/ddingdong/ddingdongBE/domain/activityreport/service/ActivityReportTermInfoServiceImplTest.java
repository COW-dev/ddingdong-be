package ddingdong.ddingdongBE.domain.activityreport.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import ddingdong.ddingdongBE.common.exception.ActivityReportException;
import ddingdong.ddingdongBE.common.fixture.ActivityReportTermInfoFixture;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.activityreport.entity.ActivityReportTermInfo;
import ddingdong.ddingdongBE.domain.activityreport.repository.ActivityReportTermInfoRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ActivityReportTermInfoServiceImplTest extends TestContainerSupport {

    @Autowired
    private ActivityReportTermInfoService activityReportTermInfoService;
    @Autowired
    private ActivityReportTermInfoRepository activityReportTermInfoRepository;

    @DisplayName("그해 첫 회차 생성이면 1회차부터 시작일 기준 2주 단위로 생성된다")
    @Test
    void createFirstTermsOfYear() {
        // when
        activityReportTermInfoService.create(LocalDate.of(2027, 3, 2), 3);

        // then
        assertThat(activityReportTermInfoRepository.findAll())
                .extracting(ActivityReportTermInfo::getTerm, ActivityReportTermInfo::getStartDate,
                        ActivityReportTermInfo::getEndDate)
                .containsExactlyInAnyOrder(
                        tuple(1, LocalDate.of(2027, 3, 2), LocalDate.of(2027, 3, 15)),
                        tuple(2, LocalDate.of(2027, 3, 16), LocalDate.of(2027, 3, 29)),
                        tuple(3, LocalDate.of(2027, 3, 30), LocalDate.of(2027, 4, 12))
                );
    }

    @DisplayName("같은 해에 이미 회차가 있으면 마지막 회차 다음 번호부터 이어서 생성된다")
    @Test
    void createTermsContinuingNumberInSameYear() {
        // given
        activityReportTermInfoRepository.saveAll(List.of(
                ActivityReportTermInfoFixture.create(1, LocalDate.of(2027, 3, 2), LocalDate.of(2027, 3, 15)),
                ActivityReportTermInfoFixture.create(2, LocalDate.of(2027, 3, 16), LocalDate.of(2027, 3, 29))
        ));

        // when
        activityReportTermInfoService.create(LocalDate.of(2027, 9, 6), 2);

        // then
        assertThat(activityReportTermInfoRepository.findAll())
                .extracting(ActivityReportTermInfo::getTerm, ActivityReportTermInfo::getStartDate)
                .containsExactlyInAnyOrder(
                        tuple(1, LocalDate.of(2027, 3, 2)),
                        tuple(2, LocalDate.of(2027, 3, 16)),
                        tuple(3, LocalDate.of(2027, 9, 6)),
                        tuple(4, LocalDate.of(2027, 9, 20))
                );
    }

    @DisplayName("해가 바뀌면 지난 해 회차 번호와 관계없이 1회차부터 생성된다")
    @Test
    void createTermsRestartNumberInNewYear() {
        // given
        activityReportTermInfoRepository.save(
                ActivityReportTermInfoFixture.create(13, LocalDate.of(2026, 11, 23), LocalDate.of(2026, 12, 6)));

        // when
        activityReportTermInfoService.create(LocalDate.of(2027, 3, 2), 1);

        // then
        assertThat(activityReportTermInfoRepository.findAll())
                .extracting(ActivityReportTermInfo::getTerm, ActivityReportTermInfo::getStartDate)
                .containsExactlyInAnyOrder(
                        tuple(13, LocalDate.of(2026, 11, 23)),
                        tuple(1, LocalDate.of(2027, 3, 2))
                );
    }

    @DisplayName("시작일이 그해 기존 회차 마감일 이전이거나 같으면 회차를 생성할 수 없다")
    @Test
    void cannotCreateTermsOverlappingExistingTerm() {
        // given
        activityReportTermInfoRepository.save(
                ActivityReportTermInfoFixture.create(1, LocalDate.of(2027, 3, 2), LocalDate.of(2027, 3, 15)));

        // when & then
        assertThatThrownBy(() -> activityReportTermInfoService.create(LocalDate.of(2027, 3, 15), 1))
                .isInstanceOf(ActivityReportException.OverlapTermPeriodException.class);
        assertThat(activityReportTermInfoRepository.count()).isEqualTo(1);
    }

    @DisplayName("같은 해 이후 학기 회차가 이미 있으면 그보다 앞선 날짜로 회차를 생성할 수 없다")
    @Test
    void cannotCreateTermsBeforeLaterTermInSameYear() {
        // given
        activityReportTermInfoRepository.save(
                ActivityReportTermInfoFixture.create(1, LocalDate.of(2027, 9, 6), LocalDate.of(2027, 9, 19)));

        // when & then
        assertThatThrownBy(() -> activityReportTermInfoService.create(LocalDate.of(2027, 3, 2), 1))
                .isInstanceOf(ActivityReportException.OverlapTermPeriodException.class);
    }

    @DisplayName("지난 해에 시작해 올해까지 이어지는 회차와 기간이 겹치면 회차를 생성할 수 없다")
    @Test
    void cannotCreateTermsOverlappingTermFromPreviousYear() {
        // given
        activityReportTermInfoRepository.save(
                ActivityReportTermInfoFixture.create(14, LocalDate.of(2026, 12, 28), LocalDate.of(2027, 1, 10)));

        // when & then
        assertThatThrownBy(() -> activityReportTermInfoService.create(LocalDate.of(2027, 1, 4), 1))
                .isInstanceOf(ActivityReportException.OverlapTermPeriodException.class);
        assertThat(activityReportTermInfoRepository.count()).isEqualTo(1);
    }

    @DisplayName("마지막 회차 마감일이 해를 넘기면 회차를 생성할 수 없다")
    @Test
    void cannotCreateTermsExceedingYear() {
        // when & then
        assertThatThrownBy(() -> activityReportTermInfoService.create(LocalDate.of(2027, 12, 19), 1))
                .isInstanceOf(ActivityReportException.TermPeriodExceedsYearException.class);
        assertThat(activityReportTermInfoRepository.count()).isZero();
    }

    @DisplayName("마지막 회차 마감일이 12월 31일이면 회차를 생성할 수 있다")
    @Test
    void createTermsEndingOnLastDayOfYear() {
        // when
        activityReportTermInfoService.create(LocalDate.of(2027, 12, 18), 1);

        // then
        assertThat(activityReportTermInfoRepository.findAll())
                .extracting(ActivityReportTermInfo::getEndDate)
                .containsExactly(LocalDate.of(2027, 12, 31));
    }
}
