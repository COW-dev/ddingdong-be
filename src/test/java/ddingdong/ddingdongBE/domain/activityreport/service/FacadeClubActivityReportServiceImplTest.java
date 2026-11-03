package ddingdong.ddingdongBE.domain.activityreport.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import ddingdong.ddingdongBE.common.exception.ActivityReportException;
import ddingdong.ddingdongBE.common.fixture.ActivityReportTermInfoFixture;
import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.UserFixture;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.activityreport.entity.ActivityReportTermStatus;
import ddingdong.ddingdongBE.domain.activityreport.repository.ActivityReportRepository;
import ddingdong.ddingdongBE.domain.activityreport.repository.ActivityReportTermInfoRepository;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.command.CreateActivityReportCommand;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.command.UpdateActivityReportCommand;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportTermInfoQuery;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
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
    @Autowired
    private ActivityReportRepository activityReportRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClubRepository clubRepository;

    private User clubUser;

    @BeforeEach
    void setUp() {
        clubUser = userRepository.save(UserFixture.createClubUser());
        clubRepository.save(ClubFixture.createClub(clubUser));
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

    @DisplayName("회차 목록은 지난 해 회차를 제외하고 올해 회차만 회차 번호 순으로 조회된다")
    @Test
    void getOnlyThisYearTermsOrderedByTerm() {
        // given
        activityReportTermInfoRepository.save(
                ActivityReportTermInfoFixture.create(13, LocalDate.of(2025, 11, 24), LocalDate.of(2025, 12, 7)));
        LocalDateTime now = LocalDateTime.of(2026, 3, 20, 12, 0);

        // when
        List<ActivityReportTermInfoQuery> termInfos = facadeClubActivityReportService.getActivityReportTermInfos(now);

        // then
        assertThat(termInfos)
                .extracting(ActivityReportTermInfoQuery::term)
                .containsExactly(1, 2, 3);
    }

    @DisplayName("오늘이 회차 기간 안이면 동아리는 활동보고서를 등록할 수 있다")
    @Test
    void createActivityReportWithinTermPeriod() {
        // given
        LocalDateTime ongoingPeriod = LocalDateTime.of(2026, 3, 20, 12, 0);

        // when
        facadeClubActivityReportService.create(clubUser, ongoingPeriod, List.of(createReportCommand(2)));

        // then
        assertThat(activityReportRepository.count()).isEqualTo(1);
    }

    @DisplayName("회차 시작일과 마감일 당일에도 동아리는 활동보고서를 등록할 수 있다")
    @Test
    void createActivityReportOnTermBoundaryDates() {
        // when
        facadeClubActivityReportService.create(clubUser, LocalDateTime.of(2026, 3, 16, 0, 0),
                List.of(createReportCommand(2)));
        facadeClubActivityReportService.create(clubUser, LocalDateTime.of(2026, 3, 29, 23, 59),
                List.of(createReportCommand(2)));

        // then
        assertThat(activityReportRepository.count()).isEqualTo(2);
    }

    @DisplayName("마감일이 지난 회차에는 동아리가 활동보고서를 등록할 수 없다")
    @Test
    void cannotCreateActivityReportForClosedTerm() {
        // given
        LocalDateTime afterTermOneClosed = LocalDateTime.of(2026, 3, 20, 12, 0);

        // when & then
        assertThatThrownBy(() -> facadeClubActivityReportService.create(clubUser, afterTermOneClosed,
                List.of(createReportCommand(1))))
                .isInstanceOf(ActivityReportException.NotSubmissionPeriodException.class);
        assertThat(activityReportRepository.count()).isZero();
    }

    @DisplayName("시작 전인 회차에는 동아리가 활동보고서를 등록할 수 없다")
    @Test
    void cannotCreateActivityReportForUpcomingTerm() {
        // given
        LocalDateTime beforeTermThreeStarts = LocalDateTime.of(2026, 3, 20, 12, 0);

        // when & then
        assertThatThrownBy(() -> facadeClubActivityReportService.create(clubUser, beforeTermThreeStarts,
                List.of(createReportCommand(3))))
                .isInstanceOf(ActivityReportException.NotSubmissionPeriodException.class);
    }

    @DisplayName("올해 존재하지 않는 회차에는 동아리가 활동보고서를 등록할 수 없다")
    @Test
    void cannotCreateActivityReportForNonExistentTerm() {
        // given
        LocalDateTime gapPeriod = LocalDateTime.of(2026, 4, 5, 12, 0);

        // when & then
        assertThatThrownBy(() -> facadeClubActivityReportService.create(clubUser, gapPeriod,
                List.of(createReportCommand(0))))
                .isInstanceOf(ActivityReportException.NotSubmissionPeriodException.class);
    }

    @DisplayName("여러 활동보고서 중 하나라도 기간 밖 회차이면 아무것도 등록되지 않는다")
    @Test
    void cannotCreateAnyActivityReportWhenOneIsOutOfPeriod() {
        // given
        LocalDateTime ongoingPeriod = LocalDateTime.of(2026, 3, 20, 12, 0);

        // when & then
        assertThatThrownBy(() -> facadeClubActivityReportService.create(clubUser, ongoingPeriod,
                List.of(createReportCommand(2), createReportCommand(1))))
                .isInstanceOf(ActivityReportException.NotSubmissionPeriodException.class);
        assertThat(activityReportRepository.count()).isZero();
    }

    @DisplayName("마감일이 지난 회차의 활동보고서는 동아리가 수정할 수 없다")
    @Test
    void cannotUpdateActivityReportForClosedTerm() {
        // given
        LocalDateTime afterTermOneClosed = LocalDateTime.of(2026, 3, 20, 12, 0);

        // when & then
        assertThatThrownBy(() -> facadeClubActivityReportService.update(clubUser, afterTermOneClosed, 1,
                List.of(updateReportCommand())))
                .isInstanceOf(ActivityReportException.NotSubmissionPeriodException.class);
    }

    @DisplayName("마감일이 지난 회차의 활동보고서는 동아리가 삭제할 수 없다")
    @Test
    void cannotDeleteActivityReportForClosedTerm() {
        // given
        LocalDateTime afterTermOneClosed = LocalDateTime.of(2026, 3, 20, 12, 0);

        // when & then
        assertThatThrownBy(() -> facadeClubActivityReportService.delete(clubUser, afterTermOneClosed, 1))
                .isInstanceOf(ActivityReportException.NotSubmissionPeriodException.class);
    }

    @DisplayName("오늘이 회차 기간 안이면 동아리는 작성한 활동보고서를 수정하고 삭제할 수 있다")
    @Test
    void updateAndDeleteActivityReportWithinTermPeriod() {
        // given
        LocalDateTime now = LocalDateTime.now();
        int ongoingTerm = 20;
        activityReportTermInfoRepository.save(ActivityReportTermInfoFixture.create(
                ongoingTerm, now.toLocalDate(), now.toLocalDate().plusDays(13)));
        facadeClubActivityReportService.create(clubUser, now, List.of(createReportCommand(ongoingTerm)));

        // when
        facadeClubActivityReportService.update(clubUser, now, ongoingTerm, List.of(updateReportCommand()));
        facadeClubActivityReportService.delete(clubUser, now, ongoingTerm);

        // then
        assertThat(activityReportRepository.count()).isZero();
    }

    private CreateActivityReportCommand createReportCommand(int term) {
        return CreateActivityReportCommand.builder()
                .term(term)
                .content("세션을 진행하였습니다")
                .place("S1353")
                .startDate("2026-03-20 10:00")
                .endDate("2026-03-20 12:00")
                .participants(List.of())
                .build();
    }

    private UpdateActivityReportCommand updateReportCommand() {
        return UpdateActivityReportCommand.builder()
                .content("수정된 내용입니다")
                .place("S1350")
                .startDate("2026-03-20 10:00")
                .endDate("2026-03-20 12:00")
                .participants(List.of())
                .build();
    }
}