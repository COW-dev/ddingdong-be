package ddingdong.ddingdongBE.domain.form.service.vo;

import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.ApplicantRateQuery;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApplicationRates {

    @Getter
    @Builder
    public static class ApplicationRate {
        private String label;
        private int count;
        private int comparedToLastSemester;

        public void updateComparedToLastSemester(int comparedToLastSemester) {
            this.comparedToLastSemester = comparedToLastSemester;
        }

        public ApplicantRateQuery toQuery() {
            return new ApplicantRateQuery(label, count, comparedToLastSemester);
        }
    }

    private final List<ApplicationRate> applicationRates = new ArrayList<>();
    private static final int DEFAULT_APPLICATION_RATE = 100;


    public void add(String label, int count, int comparedToLastSemester) {
        if (applicationRates.isEmpty()) {
            applicationRates.add(new ApplicationRate(label, count, comparedToLastSemester));
            return;
        }
        ApplicationRate lastSemester = applicationRates.get(0);
        lastSemester.updateComparedToLastSemester(comparedToLastSemester);
        applicationRates.add(0, new ApplicationRate(label, count, DEFAULT_APPLICATION_RATE));
    }

    public ApplicationRate getPrevious() {
        if (applicationRates.isEmpty()) {
            return null;
        }
        return applicationRates.get(0);
    }
}
