package ddingdong.ddingdongBE.domain.fixzone.controller.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AdminDetailFixResponse {

	private Long id;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdAt;

	private String club;

	private String location;

	private String title;

	private String content;

	private boolean isCompleted;

	private List<String> imageUrls;

	@Builder
	public AdminDetailFixResponse(Long id, LocalDateTime createdAt, String club, String location, String title,
		String content, boolean isCompleted, List<String> imageUrls) {
		this.id = id;
		this.createdAt = createdAt;
		this.club = club;
		this.location = location;
		this.title = title;
		this.content = content;
		this.isCompleted = isCompleted;
		this.imageUrls = imageUrls;
	}
}
