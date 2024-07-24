package ddingdong.ddingdongBE.domain.documents.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.file.dto.FileResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DetailDocumentResponse {

    private final String title;

    private final String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private final LocalDateTime createdAt;

    private final List<FileResponse> fileUrls;

    @Builder
    private DetailDocumentResponse(String title, String content, LocalDateTime createdAt,
                                   List<FileResponse> fileUrls) {
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.fileUrls = fileUrls;
    }

    public static DetailDocumentResponse of(Document document,
                                            List<FileResponse> fileResponses) {
        return DetailDocumentResponse.builder()
                .title(document.getTitle())
                .content(document.getContent())
                .createdAt(document.getCreatedAt())
                .fileUrls(fileResponses)
                .build();
    }
}
