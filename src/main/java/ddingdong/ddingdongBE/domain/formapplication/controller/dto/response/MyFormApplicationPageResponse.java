package ddingdong.ddingdongBE.domain.formapplication.controller.dto.response;

import ddingdong.ddingdongBE.domain.formapplication.service.dto.query.MyFormApplicationPageQuery;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.query.MyFormApplicationPageQuery.FormApplicationListQuery;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record MyFormApplicationPageResponse(
        @Schema(description = "폼지 제목", example = "카우 1기 지원 폼")
        String title,
        @Schema(description = "폼지 시작 일자", example = "2025-01-01")
        LocalDate startDate,
        @Schema(description = "폼지 마감 일자", example = "2025-02-01")
        LocalDate endDate,
        @Schema(description = "면접 여부", example = "true")
        boolean hasInterview,
        @ArraySchema(schema = @Schema(name = "지원자 전체 조회 페이지", implementation = MyFormApplicationPageResponse.MyFormApplicationListResponse.class))
        List<MyFormApplicationListResponse> formApplications,
        @Schema(name = "지원자 전체 조회 페이지 정보", implementation = PagingResponse.class)
        PagingResponse pagingInfo
) {

    public static MyFormApplicationPageResponse from(
            MyFormApplicationPageQuery myFormApplicationPageQuery) {
        List<MyFormApplicationListResponse> formApplications = myFormApplicationPageQuery.formApplicationListQueries()
                .stream()
                .map(MyFormApplicationListResponse::from)
                .toList();
        return MyFormApplicationPageResponse.builder()
                .title(myFormApplicationPageQuery.title())
                .startDate(myFormApplicationPageQuery.startDate())
                .endDate(myFormApplicationPageQuery.endDate())
                .hasInterview(myFormApplicationPageQuery.hasInterview())
                .formApplications(formApplications)
                .pagingInfo(PagingResponse.from(myFormApplicationPageQuery.pagingQuery()))
                .build();
    }

    @Builder
    record MyFormApplicationListResponse(
            @Schema(description = "지원자 id", example = "1")
            Long id,

            @Schema(description = "폼 id", example = "1")
            Long formId,

            @Schema(description = "지원 시각", example = "2025-01-01T00:00")
            LocalDateTime submittedAt,

            @Schema(description = "지원자 이름", example = "김띵동")
            String name,

            @Schema(description = "지원자 학번", example = "60200000")
            String studentNumber,

            @Schema(description = "지원자 상태", example = "SUBMITTED")
            String status
    ) {

        public static MyFormApplicationListResponse from(
                FormApplicationListQuery formApplicationListQuery) {
            return MyFormApplicationListResponse.builder()
                    .id(formApplicationListQuery.id())
                    .formId(formApplicationListQuery.formId())
                    .submittedAt(formApplicationListQuery.submittedAt())
                    .name(formApplicationListQuery.name())
                    .studentNumber(formApplicationListQuery.studentNumber())
                    .status(formApplicationListQuery.status())
                    .build();
        }
    }
}
