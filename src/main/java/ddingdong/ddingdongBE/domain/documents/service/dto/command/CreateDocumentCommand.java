package ddingdong.ddingdongBE.domain.documents.service.dto.command;

import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.domain.user.entity.User;
import lombok.Builder;

@Builder
public record CreateDocumentCommand(
    User user,
    String title
) {

    public Document toEntity() {
        return Document.builder()
            .user(user)
            .title(title)
            .build();
    }
}
