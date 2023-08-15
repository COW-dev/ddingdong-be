package ddingdong.ddingdongBE.domain.fixzone.controller.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import ddingdong.ddingdongBE.domain.fixzone.entitiy.Fix;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AdminFixResponse {

	private Long id;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdAt;

	private String club;

	private String title;

	private boolean isCompleted;

	@Builder
	public AdminFixResponse(Long id, LocalDateTime createdAt, String club, String title, boolean isCompleted) {
		this.id = id;
		this.createdAt = createdAt;
		this.club = club;
		this.title = title;
		this.isCompleted = isCompleted;
	}

	public static AdminFixResponse from(Fix fix) {
		return AdminFixResponse.builder()
			.id(fix.getId())
			.createdAt(fix.getCreatedAt())
			.club(fix.getClub().getName())
			.title(fix.getTitle())
			.isCompleted(fix.isCompleted()).build();
	}
}
