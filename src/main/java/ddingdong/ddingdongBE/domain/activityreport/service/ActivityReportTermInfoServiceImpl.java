package ddingdong.ddingdongBE.domain.activityreport.service;

import ddingdong.ddingdongBE.common.exception.ActivityReportException;
import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.activityreport.entity.ActivityReportTermInfo;
import ddingdong.ddingdongBE.domain.activityreport.entity.ActivityReportTermStatus;
import ddingdong.ddingdongBE.domain.activityreport.repository.ActivityReportTermInfoRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActivityReportTermInfoServiceImpl implements ActivityReportTermInfoService {

    private static final int NON_ACTIVITY_REPORT_TERM = 0;
    private static final long TERM_PERIOD_DAYS = 14L;

    private final ActivityReportTermInfoRepository activityReportTermInfoRepository;

    @Override
    public List<ActivityReportTermInfo> getActivityReportTermInfos(Year year) {
        return activityReportTermInfoRepository.findAllByStartDateBetweenOrderByTermAsc(
                year.atDay(1), year.atMonth(12).atEndOfMonth());
    }

    @Transactional
    @Override
    public void create(LocalDate startDate, int totalTermCount) {
        Year year = Year.from(startDate);
        // 그해 회차를 쓰기 락으로 조회해 동시 생성 요청이 같은 회차 번호를 매기지 않도록 순서대로 처리한다.
        List<ActivityReportTermInfo> termInfosOfYear = activityReportTermInfoRepository
                .findWithWriteLockByStartDateBetween(year.atDay(1), year.atMonth(12).atEndOfMonth());

        LocalDate lastTermEndDate = startDate.plusDays(totalTermCount * TERM_PERIOD_DAYS - 1);

        validateStartDateAfterLastTerm(startDate, termInfosOfYear);
        validateNotOverlappingAnyTerm(startDate, lastTermEndDate);
        validateWithinYear(lastTermEndDate, year);

        int firstTerm = getLastTerm(termInfosOfYear) + 1;
        activityReportTermInfoRepository.saveAll(
                IntStream.range(0, totalTermCount)
                        .mapToObj(index -> {
                            LocalDate termStartDate = startDate.plusDays(index * TERM_PERIOD_DAYS);
                            LocalDate termEndDate = termStartDate.plusDays(TERM_PERIOD_DAYS - 1);
                            return ActivityReportTermInfo.builder()
                                    .term(firstTerm + index)
                                    .startDate(termStartDate)
                                    .endDate(termEndDate)
                                    .build();
                        })
                        .toList()
        );
    }

    private void validateStartDateAfterLastTerm(LocalDate startDate, List<ActivityReportTermInfo> termInfosOfYear) {
        boolean overlapsExistingTerm = termInfosOfYear.stream()
                .anyMatch(termInfo -> !startDate.isAfter(termInfo.getEndDate()));
        if (overlapsExistingTerm) {
            throw new ActivityReportException.OverlapTermPeriodException();
        }
    }

    private void validateNotOverlappingAnyTerm(LocalDate startDate, LocalDate lastTermEndDate) {
        boolean overlapsExistingTerm = activityReportTermInfoRepository
                .existsByStartDateLessThanEqualAndEndDateGreaterThanEqual(lastTermEndDate, startDate);
        if (overlapsExistingTerm) {
            throw new ActivityReportException.OverlapTermPeriodException();
        }
    }

    private void validateWithinYear(LocalDate lastTermEndDate, Year year) {
        if (!Year.from(lastTermEndDate).equals(year)) {
            throw new ActivityReportException.TermPeriodExceedsYearException();
        }
    }

    private int getLastTerm(List<ActivityReportTermInfo> termInfosOfYear) {
        return termInfosOfYear.stream()
                .mapToInt(ActivityReportTermInfo::getTerm)
                .max()
                .orElse(NON_ACTIVITY_REPORT_TERM);
    }

    @Override
    public int getCurrentTerm(LocalDateTime now) {
        List<ActivityReportTermInfo> allActivityReportTermInfo = activityReportTermInfoRepository.findAll();
        return allActivityReportTermInfo.stream()
                .filter((activityReportTermInfo) -> isBelongingTerm(activityReportTermInfo.getStartDate(),
                        activityReportTermInfo.getEndDate(), now))
                .map(ActivityReportTermInfo::getTerm)
                .findFirst()
                .orElse(NON_ACTIVITY_REPORT_TERM);
    }

    @Transactional
    @Override
    public List<ActivityReportTermInfo> getAllByYearAndTermWithLock(Year year, int term) {
        List<ActivityReportTermInfo> termInfos = activityReportTermInfoRepository
                .findWithWriteLockByTermAndStartDateBetween(term, year.atDay(1), year.atMonth(12).atEndOfMonth());
        if (termInfos.isEmpty()) {
            throw new ResourceNotFound("해당 활동보고서 회차(year: " + year + ", term: " + term + ")를 찾을 수 없습니다.");
        }
        return termInfos;
    }

    @Transactional
    @Override
    public void deleteAll(List<ActivityReportTermInfo> termInfos) {
        activityReportTermInfoRepository.deleteAll(termInfos);
    }

    @Override
    public void validateSubmissionPeriod(int term, LocalDate today) {
        Year year = Year.from(today);
        boolean isOngoingTerm = activityReportTermInfoRepository
                .findWithShareLockByTermAndStartDateBetween(term, year.atDay(1), year.atMonth(12).atEndOfMonth())
                .stream()
                .anyMatch(termInfo -> termInfo.getStatus(today) == ActivityReportTermStatus.ONGOING);
        if (!isOngoingTerm) {
            throw new ActivityReportException.NotSubmissionPeriodException();
        }
    }

    private boolean isBelongingTerm(LocalDate startDate, LocalDate endDate, LocalDateTime now) {
        LocalDate nowDate = now.toLocalDate();
        return (nowDate.isEqual(startDate) || nowDate.isAfter(startDate)) &&
                (nowDate.isEqual(endDate) || nowDate.isBefore(endDate));
    }
}
