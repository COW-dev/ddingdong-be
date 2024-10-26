package ddingdong.ddingdongBE.domain.documents.service.dto.command;

import ddingdong.ddingdongBE.common.vo.FileInfo;
import ddingdong.ddingdongBE.domain.documents.entity.Document;
import java.util.List;
import lombok.Builder;

@Builder
public record UpdateDocumentCommand(
    String title,
    Long documentId,
    List<FileInfo> fileInfos
) {

    public Document toEntity() {
        return Document.builder()
            .title(title)
            .fileInfos(fileInfos)
            .build();
    }
}
