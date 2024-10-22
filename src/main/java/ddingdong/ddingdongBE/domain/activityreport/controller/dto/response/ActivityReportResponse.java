package ddingdong.ddingdongBE.domain.activityreport.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import ddingdong.ddingdongBE.domain.activityreport.domain.Participant;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record ActivityReportResponse(
    @Schema(description = "활동보고서 ID", example = "1")
    Long id,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Schema(description = "활동보고서 생성 일자", example = "2024-01-02")
    LocalDateTime createdAt,

    @Schema(description = "동아리 이름", example = "카우")
    String name,

    @Schema(description = "활동 보고서 내용", example = "세션을 진행하였습니다")
    String content,

    @Schema(description = "활동 장소", example = "S1353")
    String place,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Schema(description = "활동 시작 일자", example = "2024-01-02")
    LocalDateTime startDate,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Schema(description = "활동 종료 일자", example = "2024-01-04")
    LocalDateTime endDate,

    @Schema(description = "활동보고서 이미지 URL", implementation = ActivityReportImageUrlResponse.class)
    ActivityReportImageUrlResponse imageUrl,

    @Schema(description = "활동 참여자 목록",
        example = """
             [{
             "name" : "홍길동",
             "studentId" : "1",
             "department" : "서부서"
             }]
            """)
    List<Participant> participants
) {

    public static ActivityReportResponse from(ActivityReportQuery query) {
        return ActivityReportResponse.builder()
            .id(query.id())
            .createdAt(query.createdAt())
            .name(query.name())
            .content(query.content())
            .place(query.place())
            .startDate(query.startDate())
            .endDate(query.endDate())
            .imageUrl(ActivityReportImageUrlResponse.from(query.imageUrl()))
            .participants(query.participants())
            .build();
    }

    @Schema(
        name = "ActivityReportImageUrlResponse",
        description = "활동보고서 이미지 URL 조회 응답"
    )
    record ActivityReportImageUrlResponse(
        @Schema(description = "원본 url", example = "url")
        String originUrl,
        @Schema(description = "cdn url", example = "url")
        String cdnUrl
    ) {

        public static ActivityReportImageUrlResponse from(UploadedFileUrlQuery query) {
            return new ActivityReportImageUrlResponse(query.originUrl(), query.cdnUrl());
        }

    }
}
