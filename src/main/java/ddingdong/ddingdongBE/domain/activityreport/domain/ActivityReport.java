package ddingdong.ddingdongBE.domain.activityreport.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ddingdong.ddingdongBE.common.BaseEntity;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.request.UpdateActivityReportRequest;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityReport extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String term;

	@Column(length = 100)
	private String content;

	private String place;

	private LocalDateTime startDate;

	private LocalDateTime endDate;

	@ElementCollection
	private List<Participant> participants;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "club_id")
	private Club club;

	@Builder
	public ActivityReport(String term, String content, String place, LocalDateTime startDate, LocalDateTime endDate,
		List<Participant> participants, Club club) {
		this.term = term;
		this.content = content;
		this.place = place;
		this.startDate = startDate;
		this.endDate = endDate;
		this.participants = participants;
		this.club = club;
	}

	public void update(final UpdateActivityReportRequest updateActivityReportRequest) {
		this.content =
			updateActivityReportRequest.getContent() != null ? updateActivityReportRequest.getContent() : this.content;
		this.place =
			updateActivityReportRequest.getPlace() != null ? updateActivityReportRequest.getPlace() : this.place;
		this.startDate =
			updateActivityReportRequest.getStartDate() != null ? updateActivityReportRequest.getStartDate() :
				this.startDate;
		this.endDate =
			updateActivityReportRequest.getEndDate() != null ? updateActivityReportRequest.getEndDate() : this.endDate;
		this.participants =
			updateActivityReportRequest.getParticipants() != null ? updateActivityReportRequest.getParticipants() :
				this.participants;
	}
}
