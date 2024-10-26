package ddingdong.ddingdongBE.domain.documents.service.dto.command;

import ddingdong.ddingdongBE.common.vo.FileInfo;
import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.util.List;
import lombok.Builder;

@Builder
public record CreateDocumentCommand(
    User user,
    String title,
    List<FileInfo> fileInfos
) {

    public Document toEntity() {
        return Document.builder()
            .user(user)
            .title(title)
            .fileInfos(fileInfos)
            .build();
    }
}
