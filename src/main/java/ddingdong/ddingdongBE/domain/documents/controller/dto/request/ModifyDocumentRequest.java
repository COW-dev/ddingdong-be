package ddingdong.ddingdongBE.domain.documents.controller.dto.request;

import ddingdong.ddingdongBE.domain.documents.entity.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ModifyDocumentRequest {

    private String title;

    private String content;

    public Document toEntity() {
        return Document.builder()
                .title(title)
                .content(content)
                .build();
    }

}
