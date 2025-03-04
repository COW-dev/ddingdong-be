package ddingdong.ddingdongBE.domain.activityreport.service;

import ddingdong.ddingdongBE.domain.activityreport.entity.ActivityReportTermInfo;
import ddingdong.ddingdongBE.domain.activityreport.repository.ActivityReportTermInfoRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActivityReportTermInfoServiceImpl implements ActivityReportTermInfoService {

    private static final int NON_ACTIVITY_REPORT_TERM = 0;

    private final ActivityReportTermInfoRepository activityReportTermInfoRepository;

    @Override
    public List<ActivityReportTermInfo> getActivityReportTermInfos() {
        return activityReportTermInfoRepository.findAll();
    }

    @Transactional
    @Override
    public void create(LocalDate startDate, int totalTermCount) {
        activityReportTermInfoRepository.saveAll(
                IntStream.range(0, totalTermCount)
                        .mapToObj(i -> {
                            LocalDate termStartDate = startDate.plusDays(i * 14L);
                            LocalDate termEndDate = termStartDate.plusDays(13L);
                            return ActivityReportTermInfo.builder()
                                    .term(i + 1)
                                    .startDate(termStartDate)
                                    .endDate(termEndDate)
                                    .build();
                        })
                        .collect(Collectors.toList())
        );
    }

    @Override
    public String getCurrentTerm(LocalDateTime now) {
        List<ActivityReportTermInfo> allActivityReportTermInfo = activityReportTermInfoRepository.findAll();
        Integer currentTerm = allActivityReportTermInfo.stream()
                .filter((activityReportTermInfo) -> isBelongingTerm(activityReportTermInfo.getStartDate(),
                        activityReportTermInfo.getEndDate(), now))
                .map(ActivityReportTermInfo::getTerm)
                .findFirst()
                .orElse(NON_ACTIVITY_REPORT_TERM);
        return String.valueOf(currentTerm);
    }

    private boolean isBelongingTerm(LocalDate startDate, LocalDate endDate, LocalDateTime now) {
        LocalDate nowDate = now.toLocalDate();
        return (nowDate.isEqual(startDate) || nowDate.isAfter(startDate)) &&
                (nowDate.isEqual(endDate) || nowDate.isBefore(endDate));
    }
}
