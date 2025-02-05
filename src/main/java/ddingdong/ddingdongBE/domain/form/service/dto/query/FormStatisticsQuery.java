package ddingdong.ddingdongBE.domain.form.service.dto.query;

import ddingdong.ddingdongBE.domain.form.entity.FieldType;
import java.util.List;
import lombok.Builder;

@Builder
public record FormStatisticsQuery(
        int totalCount,
        List<DepartmentRankQuery> departmentRanks,
        List<ApplicantRateQuery> applicantRates,
        List<FieldStatisticsListQuery> fields
) {

    public record  DepartmentRankQuery(
            int rank,
            String label,
            int count,
            int rate
    ) {
    }

    public record ApplicantRateQuery(
            String label,
            int count,
            int comparedToLastSemester
    ) {


    }

    public record FieldStatisticsListQuery(
            Long id,
            String question,
            int count,
            FieldType fieldType
    ) {

    }
}
