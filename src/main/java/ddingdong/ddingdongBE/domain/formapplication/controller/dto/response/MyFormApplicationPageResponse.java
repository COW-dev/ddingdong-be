package ddingdong.ddingdongBE.domain.formapplication.controller.dto.response;

import ddingdong.ddingdongBE.domain.formapplication.service.dto.query.FormApplicationListQuery;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.query.MyFormApplicationPageQuery;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public record MyFormApplicationPageResponse (
        @ArraySchema(schema = @Schema(name = "지원자 전체 조회 페이지", implementation = MyFormApplicationPageResponse.MyFormApplicationListResponse.class))
        List<MyFormApplicationListResponse> formApplications,
        @Schema(name = "지원자 전체 조회 페이지 정보", implementation = PagingResponse.class)
        PagingResponse pagingInfo
) {
    public static MyFormApplicationPageResponse from(MyFormApplicationPageQuery myFormApplicationPageQuery) {
        List<MyFormApplicationListResponse> formApplications = myFormApplicationPageQuery.formApplicationListQueries().stream()
                .map(MyFormApplicationListResponse::from)
                .toList();
        return new MyFormApplicationPageResponse(formApplications, PagingResponse.from(myFormApplicationPageQuery.pagingQuery()));
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

        public static MyFormApplicationListResponse from(FormApplicationListQuery formApplicationListQuery) {
            return MyFormApplicationPageResponse.MyFormApplicationListResponse.builder()
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
