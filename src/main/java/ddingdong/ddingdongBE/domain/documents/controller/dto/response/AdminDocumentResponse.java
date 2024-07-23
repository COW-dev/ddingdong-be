package ddingdong.ddingdongBE.domain.documents.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import ddingdong.ddingdongBE.domain.documents.entity.Document;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AdminDocumentResponse {

    private final Long id;

    private final String title;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private final LocalDateTime createdAt;

    @Builder
    private AdminDocumentResponse(Long id, String title, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
    }

    public static AdminDocumentResponse from(Document document) {
        return AdminDocumentResponse.builder()
                .id(document.getId())
                .title(document.getTitle())
                .createdAt(document.getCreatedAt())
                .build();
    }
}
