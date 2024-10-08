package ddingdong.ddingdongBE.domain.documents.service.dto.command;

import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.util.List;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record CreateDocumentCommand(
    User user,
    String title,
    List<MultipartFile> uploadFiles
) {

    public Document toEntity() {
        return Document.builder()
            .user(user)
            .title(title)
            .build();
    }
}
