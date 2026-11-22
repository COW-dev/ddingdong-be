package ddingdong.ddingdongBE.domain.activityreport.service;

import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.common.exception.ActivityReportException;
import ddingdong.ddingdongBE.common.fixture.ActivityReportFixture;
import ddingdong.ddingdongBE.common.fixture.ActivityReportTermInfoFixture;
import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.UserFixture;
import ddingdong.ddingdongBE.common.support.NonTxTestContainerSupport;
import ddingdong.ddingdongBE.domain.activityreport.repository.ActivityReportRepository;
import ddingdong.ddingdongBE.domain.activityreport.repository.ActivityReportTermInfoRepository;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
class ActivityReportTermDeleteConcurrencyTest extends NonTxTestContainerSupport {

    private static final int TERM = 1;

    @Autowired
    private FacadeAdminActivityReportService facadeAdminActivityReportService;
    @Autowired
    private ActivityReportTermInfoService activityReportTermInfoService;
    @Autowired
    private ActivityReportTermInfoRepository activityReportTermInfoRepository;
    @Autowired
    private ActivityReportRepository activityReportRepository;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionTemplate transactionTemplate;

    @DisplayName("보고서 제출이 진행 중인 회차를 동시에 삭제하면, 삭제는 제출이 끝날 때까지 기다린 뒤 거부된다")
    @Test
    void termDeleteWaitsForOngoingSubmissionAndIsRejected() throws Exception {
        // given
        LocalDate today = LocalDate.now();
        activityReportTermInfoRepository.save(ActivityReportTermInfoFixture.create(TERM, today, today.plusDays(13)));
        Club club = clubRepository.save(ClubFixture.createClub(userRepository.save(UserFixture.createClubUser())));
        CountDownLatch submissionHoldsTermLock = new CountDownLatch(1);

        // when
        CompletableFuture<Void> submission = CompletableFuture.runAsync(() -> transactionTemplate.executeWithoutResult(
                status -> {
                    activityReportTermInfoService.validateSubmissionPeriod(TERM, today);
                    activityReportRepository.save(ActivityReportFixture.create(club, TERM));
                    submissionHoldsTermLock.countDown();
                    sleepWhileHoldingLock();
                }));
        submissionHoldsTermLock.await(5, TimeUnit.SECONDS);
        CompletableFuture<Throwable> termDelete = CompletableFuture.supplyAsync(() -> {
            try {
                facadeAdminActivityReportService.deleteActivityTermInfo(LocalDateTime.now(), TERM);
                return null;
            } catch (RuntimeException exception) {
                return exception;
            }
        });

        // then
        submission.get(10, TimeUnit.SECONDS);
        assertThat(termDelete.get(10, TimeUnit.SECONDS))
                .isInstanceOf(ActivityReportException.SubmittedTermDeleteException.class);
        assertThat(activityReportTermInfoRepository.count()).isEqualTo(1);
        assertThat(activityReportRepository.count()).isEqualTo(1);
    }

    private void sleepWhileHoldingLock() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }
}
