package ddingdong.ddingdongBE.domain.activityreport.service;

import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportTermInfoResponse;
import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReportTermInfo;
import ddingdong.ddingdongBE.domain.activityreport.repository.ActivityReportTermInfoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityReportTermInfoService {

    private final ActivityReportTermInfoRepository activityReportTermInfoRepository;

    public List<ActivityReportTermInfoResponse> getAll() {
        List<ActivityReportTermInfo> termInfos = activityReportTermInfoRepository.findAll();

        return termInfos.stream()
                .map(termInfo -> new ActivityReportTermInfoResponse(
                        termInfo.getTerm(),
                        termInfo.getStartDate(),
                        termInfo.getEndDate()
                ))
                .toList();
    }

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

}
