package ddingdong.ddingdongBE.domain.activityreport.service;

import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.common.exception.ActivityReportException;
import ddingdong.ddingdongBE.common.fixture.ActivityReportTermInfoFixture;
import ddingdong.ddingdongBE.common.support.NonTxTestContainerSupport;
import ddingdong.ddingdongBE.domain.activityreport.entity.ActivityReportTermInfo;
import ddingdong.ddingdongBE.domain.activityreport.repository.ActivityReportTermInfoRepository;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
class ActivityReportTermCreateConcurrencyTest extends NonTxTestContainerSupport {

    private static final LocalDate START_DATE = LocalDate.of(2027, 3, 2);
    private static final int TOTAL_TERM_COUNT = 3;

    @Autowired
    private ActivityReportTermInfoService activityReportTermInfoService;
    @Autowired
    private ActivityReportTermInfoRepository activityReportTermInfoRepository;
    @Autowired
    private TransactionTemplate transactionTemplate;

    @DisplayName("같은 회차 생성 요청이 동시에 들어오면 나중 요청은 앞선 생성이 끝날 때까지 기다린 뒤 거부되어 회차 번호가 중복되지 않는다")
    @Test
    void concurrentTermCreationDoesNotDuplicateTermNumbers() throws Exception {
        // given
        activityReportTermInfoRepository.save(ActivityReportTermInfoFixture.create(
                13, LocalDate.of(2026, 11, 23), LocalDate.of(2026, 12, 6)));
        CountDownLatch firstCreationHoldsLock = new CountDownLatch(1);

        // when
        CompletableFuture<Void> firstCreation = CompletableFuture.runAsync(() ->
                transactionTemplate.executeWithoutResult(status -> {
                    activityReportTermInfoService.create(START_DATE, TOTAL_TERM_COUNT);
                    firstCreationHoldsLock.countDown();
                    sleepWhileHoldingLock();
                }));
        firstCreationHoldsLock.await(5, TimeUnit.SECONDS);
        CompletableFuture<Throwable> secondCreation = CompletableFuture.supplyAsync(() -> {
            try {
                activityReportTermInfoService.create(START_DATE, TOTAL_TERM_COUNT);
                return null;
            } catch (RuntimeException exception) {
                return exception;
            }
        });

        // then
        firstCreation.get(10, TimeUnit.SECONDS);
        assertThat(secondCreation.get(10, TimeUnit.SECONDS))
                .isInstanceOf(ActivityReportException.OverlapTermPeriodException.class);
        assertThat(activityReportTermInfoRepository.findAll())
                .filteredOn(termInfo -> termInfo.getStartDate().getYear() == 2027)
                .extracting(ActivityReportTermInfo::getTerm)
                .containsExactlyInAnyOrder(1, 2, 3);
    }

    private void sleepWhileHoldingLock() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }
}
