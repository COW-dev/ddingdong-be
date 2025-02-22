package ddingdong.ddingdongBE.domain.form.service.dto.query;

import ddingdong.ddingdongBE.domain.form.entity.FieldType;
import ddingdong.ddingdongBE.domain.form.repository.dto.FieldListInfo;
import java.util.List;
import lombok.Builder;

@Builder
public record FormStatisticsQuery(
        int totalCount,
        List<DepartmentStatisticQuery> departmentStatisticQueries,
        List<ApplicantStatisticQuery> applicantStatisticQueries,
        FieldStatisticsQuery fieldStatisticsQuery
) {

    public record DepartmentStatisticQuery(
            int rank,
            String label,
            int count,
            int ratio
    ) {
    }

    public record ApplicantStatisticQuery(
            String label,
            int count,
            int compareRatio,
            int compareValue
    ) {
    }

    public record FieldStatisticsQuery(
            List<String> sections,
            List<FieldStatisticsListQuery> fieldStatisticsListQueries
    ) {

        public record FieldStatisticsListQuery(
                Long id,
                String question,
                int count,
                FieldType fieldType,
                String section
        ) {
            public static FieldStatisticsListQuery from(FieldListInfo fieldListInfo) {
                return new FieldStatisticsListQuery(
                        fieldListInfo.getId(),
                        fieldListInfo.getQuestion(),
                        fieldListInfo.getCount(),
                        fieldListInfo.getType(),
                        fieldListInfo.getSection()
                );
            }
        }
    }
}
