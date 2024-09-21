package ddingdong.ddingdongBE.domain.activityreport.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReport;
import ddingdong.ddingdongBE.domain.activityreport.domain.Participant;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record ActivityReportResponse(
		Long id,
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
		LocalDateTime createdAt,
		String name,
		String content,
		String place,
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
		LocalDateTime startDate,
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
		LocalDateTime endDate,
		List<String> ImageUrls,
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
