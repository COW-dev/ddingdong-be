package ddingdong.ddingdongBE.domain.fixzone.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "update fix_zone set deleted_at = CURRENT_TIMESTAMP where id=?")
@SQLRestriction("deleted_at IS NULL")
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

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

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
