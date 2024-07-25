package ddingdong.ddingdongBE.domain.fixzone.controller.dto.request;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateFixZoneRequest {

	private String title;

	private String content;

	public FixZone toEntity(Club club) {
		return FixZone.builder()
			.title(title)
			.content(content)
			.club(club)
			.isCompleted(false).build();
	}

}
