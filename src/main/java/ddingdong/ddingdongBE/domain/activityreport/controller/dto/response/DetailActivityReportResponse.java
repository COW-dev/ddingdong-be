package ddingdong.ddingdongBE.domain.activityreport.controller.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReport;
import ddingdong.ddingdongBE.domain.activityreport.domain.Participant;
import lombok.Getter;

@Getter
public class DetailActivityReportResponse {

	private Long id;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdAt;
	private String name;
	private String content;
	private String place;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime startDate;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime endDate;
	private List<String> imageUrls;
	private List<Participant> participants;

	public DetailActivityReportResponse(Long id, String name, String content, String place, LocalDateTime startDate,
		LocalDateTime endDate, List<String> imageUrls, List<Participant> participants, LocalDateTime createdAt) {
		this.id = id;
		this.name = name;
		this.content = content;
		this.place = place;
		this.startDate = startDate;
		this.endDate = endDate;
		this.imageUrls = imageUrls;
		this.participants = participants;
		this.createdAt = createdAt;
	}

	public static DetailActivityReportResponse from(ActivityReport activityReport, List<String> imageUrls) {
		return new DetailActivityReportResponse(
			activityReport.getId(),
			activityReport.getClub().getName(),
			activityReport.getContent(),
			activityReport.getPlace(),
			activityReport.getStartDate(),
			activityReport.getEndDate(),
			imageUrls,
			activityReport.getParticipants(),
			activityReport.getCreatedAt()
		);
	}
}
