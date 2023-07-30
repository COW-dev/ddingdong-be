package ddingdong.ddingdongBE.file.dto;

import ddingdong.ddingdongBE.domain.fileinformation.entity.FileInformation;
import lombok.Getter;

@Getter
public class FileResponse {
    private String name;
    private String fileUrl;

    public FileResponse(String name, String fileUrl) {
        this.name = name;
        this.fileUrl = fileUrl;
    }

    public static FileResponse from(FileInformation fileInformation, String fileUrl) {
        return new FileResponse(fileInformation.getUploadName(), fileUrl);
    }
}
