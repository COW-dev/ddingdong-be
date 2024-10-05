package ddingdong.ddingdongBE.domain.documents.service.dto.command;

import ddingdong.ddingdongBE.domain.documents.entity.Document;
import java.util.List;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record UpdateDocumentCommand(
    String title,
    Long documentId,
    List<MultipartFile> uploadFiles
) {

    public Document toEntity() {
        return Document.builder()
            .title(title)
            .build();
    }
}
