package ddingdong.ddingdongBE.domain.fixzone.controller.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ClubDetailFixResponse {

	private Long id;

	private String title;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdAt;

	private String content;

	private boolean isCompleted;

	private List<String> imageUrls;

	@Builder
	public ClubDetailFixResponse(Long id, String title, LocalDateTime createdAt, String content, boolean isCompleted,
		List<String> imageUrls) {
		this.id = id;
		this.title = title;
		this.createdAt = createdAt;
		this.content = content;
		this.isCompleted = isCompleted;
		this.imageUrls = imageUrls;
	}
}
