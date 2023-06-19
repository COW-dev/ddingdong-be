package ddingdong.ddingdongBE.file;

import ddingdong.ddingdongBE.file.dto.UploadFileDto;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface FileStore {

    List<UploadFileDto> storeFile(List<MultipartFile> multipartFile, String filePath);

    void deleteFile(String filePath, String uploadFileName);

    String getImageUrlPrefix();

}
