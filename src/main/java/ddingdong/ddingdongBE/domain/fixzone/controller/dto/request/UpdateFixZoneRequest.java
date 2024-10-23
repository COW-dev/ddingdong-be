package ddingdong.ddingdongBE.domain.fixzone.controller.dto.request;

import ddingdong.ddingdongBE.domain.fixzone.service.dto.command.CreateFixZoneCommand;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.command.UpdateFixZoneCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record UpdateFixZoneRequest(
		@NotNull
		@Schema(description = "제목")
		String title,
		@NotNull
		@Schema(description = "내용")
		String content,
		@Schema(description = "픽스존 내용 이미지 키", example = "prod/file/2024-01-01/uuid")
		List<String> fixZoneImageKeys
) {

	public UpdateFixZoneCommand toCommand(Long fixZoneId) {
		return new UpdateFixZoneCommand(
				fixZoneId,
				title,
				content,
				fixZoneImageKeys
		);
	}

}
