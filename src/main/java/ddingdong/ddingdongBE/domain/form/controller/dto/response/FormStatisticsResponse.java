package ddingdong.ddingdongBE.domain.form.controller.dto.response;

import ddingdong.ddingdongBE.domain.form.entity.FieldType;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.ApplicantStatisticQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.DepartmentStatisticQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.FieldStatisticsQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.FieldStatisticsQuery.FieldStatisticsListQuery;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Builder
public record FormStatisticsResponse(
        @Schema(description = "총 지원자 수", example = "50")
        int totalCount,
        @ArraySchema(schema = @Schema(implementation = DepartmentStatisticResponse.class))
        List<DepartmentStatisticResponse> departmentStatistics,
        @ArraySchema(schema = @Schema(implementation = ApplicantStatisticResponse.class))
        List<ApplicantStatisticResponse> applicantStatistics,
        @Schema(description = "필드 통계 전체조회", implementation = FieldStatisticsResponse.class)
        FieldStatisticsResponse fieldStatistics
) {

    @Builder
    record DepartmentStatisticResponse(
            @Schema(description = "학과 내 경쟁 순위", example = "1")
            int rank,
            @Schema(description = "학과명", example = "융합소프트웨어학부")
            String label,
            @Schema(description = "해당 학과의 지원자 수", example = "50")
            int count,
            @Schema(description = "전체 지원자 수 대비 비율", example = "30")
            int ratio
    ) {

        public static DepartmentStatisticResponse from(DepartmentStatisticQuery query) {
            return DepartmentStatisticResponse.builder()
                    .rank(query.rank())
                    .label(query.label())
                    .count(query.count())
                    .ratio(query.ratio())
                    .build();
        }
    }

    @Builder
    record ApplicantStatisticResponse(
            @Schema(description = "비교 년도 및 월", example = "2025-1")
            String label,
            @Schema(description = "해당 년도 및 학기 총 지원자수", example = "40")
            int count,
            @Schema(description = "전 폼지 대비 증감 값", example = "150")
            CompareToBefore comparedToBefore
    ) {

        record CompareToBefore(
                @Schema(description = "증감율 %", example = "50")
                int ratio,
                @Schema(description = "증가수치 및 감소수치", example = "15")
                int value
        ) {

        }

        public static ApplicantStatisticResponse from(ApplicantStatisticQuery query) {
            return ApplicantStatisticResponse.builder()
                    .label(query.label())
                    .count(query.count())
                    .comparedToBefore(
                            new CompareToBefore(query.compareRatio(), query.compareValue()))
                    .build();
        }
    }

    @Builder
    record FieldStatisticsResponse(
            @Schema(description = "섹션종류", example = "[\"공통\"]")
            List<String> sections,
            @ArraySchema(schema = @Schema(implementation = FieldStatisticsListResponse.class))
            List<FieldStatisticsListResponse> fields
    ) {

        record FieldStatisticsListResponse(
                @Schema(description = "폼지 질문 id", example = "1")
                Long id,
                @Schema(description = "폼지 질문", example = "당신 이름은 무엇인가요?")
                String question,
                @Schema(description = "폼지 질문에 대해 총 작성 개수", example = "20")
                int count,
                @Schema(description = "폼지 질문 유형", example = "CHECK_BOX")
                FieldType type,
                @Schema(description = "섹션", example = "공통")
                String section
        ) {

            public static FieldStatisticsListResponse from(FieldStatisticsListQuery query) {
                return new FieldStatisticsListResponse(query.id(), query.question(), query.count(),
                        query.fieldType(),
                        query.section());
            }
        }

        public static FieldStatisticsResponse from(FieldStatisticsQuery query) {
            List<FieldStatisticsListResponse> fieldStatisticsListResponses = query.fieldStatisticsListQueries()
                    .stream()
                    .map(FieldStatisticsListResponse::from)
                    .toList();
            return FieldStatisticsResponse.builder()
                    .sections(query.sections())
                    .fields(fieldStatisticsListResponses)
                    .build();
        }
    }

    public static FormStatisticsResponse from(FormStatisticsQuery query) {
        List<DepartmentStatisticResponse> departmentStatisticResponse = query.departmentStatisticQueries()
                .stream()
                .map(DepartmentStatisticResponse::from)
                .toList();
        List<ApplicantStatisticResponse> applicantStatisticResponse = query.applicantStatisticQueries()
                .stream()
                .map(ApplicantStatisticResponse::from)
                .toList();
        return FormStatisticsResponse.builder()
                .totalCount(query.totalCount())
                .departmentStatistics(departmentStatisticResponse)
                .applicantStatistics(applicantStatisticResponse)
                .fieldStatistics(FieldStatisticsResponse.from(query.fieldStatisticsQuery()))
                .build();
    }
}
