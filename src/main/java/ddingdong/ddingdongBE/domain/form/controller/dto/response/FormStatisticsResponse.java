package ddingdong.ddingdongBE.domain.form.controller.dto.response;

import ddingdong.ddingdongBE.domain.form.entity.FieldType;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.ApplicantRateQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.DepartmentRankQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.FieldStatisticsListQuery;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Builder
public record FormStatisticsResponse(
        @Schema(description = "총 지원자 수", example = "50")
        int totalCount,
        @ArraySchema(schema = @Schema(implementation = DepartmentRankResponse.class))
        List<DepartmentRankResponse> departmentRanks,
        @ArraySchema(schema = @Schema(implementation = ApplicantRateResponse.class))
        List<ApplicantRateResponse> applicantRates,
        @ArraySchema(schema = @Schema(implementation = FieldStatisticsListResponse.class))
        List<FieldStatisticsListResponse> fields
) {

    @Builder
    record  DepartmentRankResponse(
            @Schema(description = "학과 내 경쟁 순위", example = "1")
            int rank,
            @Schema(description = "학과명", example = "융합소프트웨어학부")
            String label,
            @Schema(description = "해당 학과의 지원자 수", example = "50")
            int count,
            @Schema(description = "전체 지원자 수 대비 비율", example = "30")
            int rate
    ) {

        public static DepartmentRankResponse from(DepartmentRankQuery query) {
            return DepartmentRankResponse.builder()
                    .rank(query.rank())
                    .label(query.label())
                    .count(query.count())
                    .rate(query.rate())
                    .build();
        }
    }

    @Builder
    record ApplicantRateResponse(
            @Schema(description = "비교 년도 및 학기", example = "2025-1")
            String label,
            @Schema(description = "해당 년도 및 학기 총 지원자수", example = "40")
            int count,
            @Schema(description = "비교 대비 비율", example = "150")
            int comparedToLastSemester
    ) {
        public static ApplicantRateResponse from(ApplicantRateQuery query) {
            return ApplicantRateResponse.builder()
                    .label(query.label())
                    .count(query.count())
                    .comparedToLastSemester(query.comparedToLastSemester())
                    .build();
        }

    }

    @Builder
    record FieldStatisticsListResponse(
            @Schema(description = "폼지 질문 id", example = "1")
            Long id,
            @Schema(description = "폼지 질문", example = "당신 이름은 무엇인가요?")
            String question,
            @Schema(description = "폼지 질문에 대해 총 작성 개수", example = "20")
            int count,
            @Schema(description = "폼지 질문 유형", example = "CHECK_BOX")
            FieldType type
    ) {

        public static FieldStatisticsListResponse from(FieldStatisticsListQuery query) {
            return FieldStatisticsListResponse.builder()
                    .id(query.id())
                    .question(query.question())
                    .count(query.count())
                    .type(query.fieldType())
                    .build();
        }
    }

    public static FormStatisticsResponse from(FormStatisticsQuery query) {
        List<DepartmentRankResponse> departmentRankResponses = query.departmentRanks().stream()
                .map(DepartmentRankResponse::from)
                .toList();
        List<ApplicantRateResponse> applicantRateResponses = query.applicantRates().stream()
                .map(ApplicantRateResponse::from)
                .toList();
        List<FieldStatisticsListResponse> fieldStatisticsListResponses = query.fields().stream()
                .map(FieldStatisticsListResponse::from)
                .toList();
        return FormStatisticsResponse.builder()
                .totalCount(query.totalCount())
                .departmentRanks(departmentRankResponses)
                .applicantRates(applicantRateResponses)
                .fields(fieldStatisticsListResponses)
                .build();
    }
}
