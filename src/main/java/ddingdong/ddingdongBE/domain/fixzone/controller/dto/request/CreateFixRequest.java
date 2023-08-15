package ddingdong.ddingdongBE.domain.fixzone.controller.dto.request;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.fixzone.entitiy.Fix;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateFixRequest {

	private String title;

	private String content;

	public Fix toEntity(Club club) {
		return Fix.builder()
			.title(title)
			.content(content)
			.club(club)
			.isCompleted(false).build();
	}

}
