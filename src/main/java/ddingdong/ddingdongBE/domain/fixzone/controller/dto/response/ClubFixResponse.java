package ddingdong.ddingdongBE.domain.fixzone.controller.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import ddingdong.ddingdongBE.domain.fixzone.entitiy.Fix;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ClubFixResponse {

	private Long id;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdAt;

	private String title;

	private boolean isCompleted;

	@Builder
	public ClubFixResponse(Long id, String title, LocalDateTime createdAt, boolean isCompleted) {
		this.id = id;
		this.createdAt = createdAt;
		this.title = title;
		this.isCompleted = isCompleted;
	}

	public static ClubFixResponse from(Fix fix) {
		return ClubFixResponse.builder()
			.id(fix.getId())
			.createdAt(fix.getCreatedAt())
			.title(fix.getTitle())
			.isCompleted(fix.isCompleted()).build();
	}

}
