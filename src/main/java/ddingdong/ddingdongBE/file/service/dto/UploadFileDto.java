package ddingdong.ddingdongBE.file.service.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UploadFileDto {

    private String uploadFileName;

    private String storedFileName;

    @Builder
    public UploadFileDto(String uploadFileName, String storedFileName) {
        this.uploadFileName = uploadFileName;
        this.storedFileName = storedFileName;
    }

}
