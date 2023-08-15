package ddingdong.ddingdongBE.domain.fixzone.entitiy;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ddingdong.ddingdongBE.common.BaseEntity;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.request.UpdateFixRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Fix extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "club_id")
	private Club club;

	private String title;

	private String content;

	private boolean isCompleted;

	@Builder
	private Fix(Long id, Club club, String title, String content, boolean isCompleted) {
		this.id = id;
		this.club = club;
		this.title = title;
		this.content = content;
		this.isCompleted = isCompleted;
	}

	public void update(UpdateFixRequest request) {
		this.title = request.getTitle() != null ? request.getTitle() : this.title;
		this.content = request.getContent() != null ? request.getContent() : this.content;
	}
}
