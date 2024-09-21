package ddingdong.ddingdongBE.domain.activityreport.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReport;
import ddingdong.ddingdongBE.domain.activityreport.domain.Participant;
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

		@Schema(description = "활동 이미지 URL 목록",
				example = """
		[
		"https://example.com/image1.jpg", 
		"https://example.com/image2.jpg"]
		""")
		List<String> ImageUrls,

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

	public static ActivityReportResponse of(ActivityReport activityReport, List<String> imageUrls) {
		return ActivityReportResponse.builder()
				.id(activityReport.getId())
				.createdAt(activityReport.getCreatedAt())
				.name(activityReport.getClub().getName())
				.content(activityReport.getContent())
				.place(activityReport.getPlace())
				.startDate(activityReport.getStartDate())
				.endDate(activityReport.getEndDate())
				.ImageUrls(imageUrls)
				.participants(activityReport.getParticipants())
				.build();
	}
}
