package ddingdong.ddingdongBE.domain.activityreport.service;

import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReportTermInfo;
import ddingdong.ddingdongBE.domain.activityreport.repository.ActivityReportTermInfoRepository;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActivityReportTermInfoService {

  private static final String START_DATE = "2024-09-02";
  private static final int DEFAULT_TERM = 8;
  private static final int CORRECTION_VALUE = 8;
  private static final int TERM_LENGTH_OF_DAYS = 14;

  private final ActivityReportTermInfoRepository activityReportTermInfoRepository;

  public List<ActivityReportTermInfo> getActivityReportTermInfos() {
    return activityReportTermInfoRepository.findAll();
  }

  @Transactional
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

  public String getCurrentTerm() {
    LocalDate startDate = LocalDate.parse(START_DATE);
    LocalDate currentDate = LocalDate.now();

    int gapOfDays = calculateGapOfDays(startDate, currentDate);
    return calculateCurrentTerm(gapOfDays);
  }

  private int calculateGapOfDays(final LocalDate startDate, final LocalDate currentDate) {
    return (int) Duration.between(startDate.atStartOfDay(), currentDate.atStartOfDay())
        .toDays();
  }

  private String calculateCurrentTerm(final int days) {
    int result = CORRECTION_VALUE + (days / TERM_LENGTH_OF_DAYS);

    if (result <= CORRECTION_VALUE) {
      result = DEFAULT_TERM;
    }

    return String.valueOf(result);
  }
}
