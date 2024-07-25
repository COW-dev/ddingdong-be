package ddingdong.ddingdongBE.domain.fixzone.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FixZone extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "club_id")
	private Club club;

	@OneToMany(mappedBy = "fixZone", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<FixZoneComment> fixZoneComments = new ArrayList<>();

	private String title;

	private String content;

	private boolean isCompleted;

	@Builder
	private FixZone(Long id, Club club, String title, String content, boolean isCompleted) {
		this.id = id;
		this.club = club;
		this.title = title;
		this.content = content;
		this.isCompleted = isCompleted;
	}

	public void update(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public void updateToComplete() {
		this.isCompleted = true;
	}

}
