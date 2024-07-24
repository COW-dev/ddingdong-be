package ddingdong.ddingdongBE.domain.documents.controller.dto.response;

import ddingdong.ddingdongBE.domain.fileinformation.entity.FileInformation;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DocumentFileInformationResponse {

    private final String fileName;

    private final String fileUrl;

    @Builder
    private DocumentFileInformationResponse(String fileName, String fileUrl) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }

    public static DocumentFileInformationResponse of(String fileName, String fileUrl) {
        return DocumentFileInformationResponse.builder()
                .fileName(fileName)
                .fileUrl(fileUrl)
                .build();
    }
}
